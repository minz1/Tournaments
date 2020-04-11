fun main(args: Array<String>) {
    print("Please enter the amount of participants you will have in the tournament (Must be greater than 1): ")

    var numParticipants: Int = readLine()?.toIntOrNull() ?: 0

    while (numParticipants < 2) {
        print("INPUT ERROR: Please try again: ")
        numParticipants = readLine()?.toIntOrNull() ?: 0
    }

    print("Please enter how many matches can be run at once (Must be greater than 0): ")

    var numConcurrentMatches: Int = readLine()?.toIntOrNull() ?: 0

    while (numConcurrentMatches < 1) {
        print("INPUT ERROR: Please try again: ")
        numConcurrentMatches = readLine()?.toIntOrNull() ?: 0
    }

    var participants = ArrayList<Participant>(numParticipants)

    for (i in 0 until numParticipants) {
        print("Please enter the name of Participant ${i + 1}: ")
        var name = readLine() ?: ""

        while (name.isNullOrEmpty()) {
            print("INPUT ERROR: Please input a name for Participant ${i + 1}: ")
            name = readLine() ?: ""
        }

        participants.add(Participant(name, i))
    }

    var tournament = TournamentTree(participants.toList(), numConcurrentMatches)

    println("Players: ${tournament.numParticipants} \nByes: ${tournament.numByes}")

    val matches = tournament.getNextMatches()

    for (i in matches.indices) {
        println("Contestant ${matches[i].contestant1.seed}: ${matches[i].contestant1.name}\n" +
                "Contestant ${matches[i].contestant2.seed}: ${matches[i].contestant2.name}")
    }
}