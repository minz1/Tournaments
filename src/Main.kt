fun main(args: Array<String>) {
    print("Please enter the amount of participants you will have in the tournament (Must be greater than 2): ")

    var numParticipants: Int = readLine()?.toIntOrNull() ?: 0

    while (numParticipants < 2) {
        print("INPUT ERROR: Please try again: ")
        numParticipants = readLine()?.toIntOrNull() ?: 0
    }

    print("Please enter how many participants will be in each match (Must be greater than 2): ")

    var numParticipantsPerMatch: Int = readLine()?.toIntOrNull() ?: 0

    while (numParticipantsPerMatch < 2) {
        print("INPUT ERROR: Please try again: ")
        numParticipantsPerMatch = readLine()?.toIntOrNull() ?: 0
    }

    while (numParticipantsPerMatch > numParticipants) {
        print("ERROR: You may have more than ${numParticipants} per match! Please try again: ")
        numParticipantsPerMatch = readLine()?.toIntOrNull() ?: 0
    }

    val participants = ArrayList<Participant>(numParticipants)

    for (i in 0 until numParticipants) {
        print("Please enter the name of Participant ${i + 1}: ")
        var name = readLine() ?: ""

        while (name.isNullOrEmpty()) {
            print("INPUT ERROR: Please input a name for Participant ${i + 1}: ")
            name = readLine() ?: ""
        }

        participants.add(Participant(name))
    }

    val tournament = TournamentTree(participants, numParticipantsPerMatch)

    println("Players: ${tournament.numParticipants} \nByes: ${tournament.numByes} \nMatches: ${tournament.numMatches}")

    var match = tournament.getNextMatch()

    if (match != null) {
        for (participant in match.participants) {
            println(participant.name)
        }
    }
}