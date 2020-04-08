@OptIn(ExperimentalStdlibApi::class)
class TournamentTree(participants: List<Participant>, participantsPerMatch: Int = 2) {
    private lateinit var rootNode: MatchNode

    val participants = participants
    val numParticipants: Int
        get() = participants.size
    val numByes: Int
        get() = getNextPowerOf2(numMatches) - numMatches
    var numMatches: Int = 0
        private set

    private fun runPostOrderTraversal(matchNode: MatchNode): MatchNode? {
        if (matchNode == null) {
            return null
        }

        if (matchNode.match.isComplete) {
            return null
        }

        if (matchNode.hasLeft) {
            if (! matchNode.left!!.match.isComplete)
                return runPostOrderTraversal(matchNode.left!!)
        }

        if (matchNode.hasRight) {
            if (! matchNode.right!!.match.isComplete)
                return runPostOrderTraversal(matchNode.right!!)
        }

        return matchNode
    }

    public fun getNextMatch(): Match? {
        return runPostOrderTraversal(rootNode)!!.match
    }

    init {
        require(participants.size > 1) {
            "The amount of participants in the tournament must be greater than 1"
        }

        require(participantsPerMatch > 1) {
            "The amount of participants per match must be greater than 1"
        }

        val numFullMatches = participants.size / participantsPerMatch
        val matches = ArrayDeque<MatchNode>(numFullMatches + 1)

        for (i in 0 until numFullMatches) {
            val matchParticipants = ArrayList<Participant>(participantsPerMatch)

            for (x in 0 until participantsPerMatch) {
                matchParticipants.add(participants[(i * 2) + x])
            }

            matches.addLast(MatchNode(Match(matchParticipants)))
        }

        val numRemainingParticipants = participants.size % participantsPerMatch
        if (numRemainingParticipants > 0) {
            val remainingParticipants = ArrayList<Participant>(numRemainingParticipants)

            for (i in (participants.size - numRemainingParticipants) until participants.size) {
                remainingParticipants.add(participants[i])
            }

            matches.addLast(MatchNode(Match(remainingParticipants)))
        }

        numMatches = matches.size

        while (matches.size > 1) {
            val first = matches.removeFirst()
            val second = matches.removeFirst()

            val temp = MatchNode()
            temp.left = first
            temp.right = second

            matches.addLast(temp)
        }

        rootNode = matches.removeFirst()
    }

    private fun getNextPowerOf2(number: Int): Int {
        var num = number - 1
        num = num or (num shr 1)
        num = num or (num shr 2)
        num = num or (num shr 4)
        num = num or (num shr 8)
        num = num or (num shr 16)
        num++
        return num
    }
}