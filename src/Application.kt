package com.minz

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.content.files
import io.ktor.http.content.static

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@OptIn(ExperimentalStdlibApi::class)
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val client = HttpClient(Apache) {
    }

    val numDummyParticipants = 8
    val testParticipants = ArrayList<Participant>(numDummyParticipants)

    for (i in 0 until numDummyParticipants) {
        testParticipants.add(Participant("P$i", i))
    }

    var tournament = TournamentTree(testParticipants)

    routing {
        route("/") {
            get {
                call.respondHtml {
                    head {
                        title { +"Tournament Form" }
                    }
                    body {
                        p {
                            +"Participants:"
                        }

                        form(action = "/", method = FormMethod.post, encType = FormEncType.applicationXWwwFormUrlEncoded) {
                            id = "tournament-creation-form"
                            textArea(rows = "50", cols = "50") {
                                name = "participants"
                                form = "tournament-creation-form"
                                required = true
                            }
                            br
                            input(type = InputType.submit) {
                                value = "Generate Tournament!"
                            }
                        }
                    }
                }
            }

            post {
                val post = call.receiveParameters()

                if (post["participants"] != null) {
                    val participants = post["participants"]!!.lines().toMutableList()

                    participants.removeIf() {
                        it.isNullOrBlank()
                    }

                    if (participants.size < 2) {
                        call.respondRedirect("/error", permanent = true)
                        return@post
                    }

                    val participantObjs = ArrayList<Participant>(participants.size)

                    for (i in participants.indices) {
                        val participantName = participants[i]

                        if (participantName.isNotBlank()) {
                            participantObjs.add(Participant(participantName, i))
                        }
                    }

                    tournament = TournamentTree(participantObjs)

                    call.respondRedirect("/visualization", permanent = true)
                }
            }
        }

        get("/visualization") {
            call.respondHtml {
                head {
                    title { +"Tournament Visualization" }
                    meta { charset = "utf-8" }
                    meta {
                        name = "viewport"
                        content = "width=device-width, initial-scale=1"
                    }
                    link {
                        rel="stylesheet"
                        href="static/tournament.css"
                    }
                }
                body {
                    main {
                        id = "tournament"
                        val rounds = tournament.getParticipantsByRound()
                        for (i in rounds.indices) {
                            val round = rounds[i]
                            ul(classes = "round round-${i + 1}") {
                                for (x in round.indices step 2) {
                                    li(classes = "spacer") {
                                        +Entities.nbsp
                                    }

                                    li(classes = "game game-top") {
                                        +round[x].name
                                    }

                                    if ((x + 1) < round.size) {
                                        li(classes = "game game-spacer") {
                                            +Entities.nbsp
                                        }
                                        li(classes = "game game-bottom") {
                                            +round[x + 1].name
                                        }
                                    }

                                    li(classes = "spacer") {
                                        +Entities.nbsp
                                    }
                                }
                            }
                        }
                    }
                    a(href="/") {
                        +"Return to home"
                    }
                }
            }
        }

        get("/error") {
            call.respondHtml {
                head {
                    title { + "ERROR"}
                }

                body {
                    h1 {
                        +"ERROR"
                    }
                    a(href="/") {
                        +"Return to home"
                    }
                }
            }
        }

        static("static") {
            files("css")
        }
    }
}