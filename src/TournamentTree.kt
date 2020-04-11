@OptIn(ExperimentalStdlibApi::class)
class TournamentTree(initialParticipants: List<Participant>, numConcurrentMatches: Int = 1) {
    private var rootNode: ParticipantNode

    private val participants = initialParticipants
    val numParticipants: Int
        get() = participants.size
    val numByes: Int
        get() = getNextPowerOf2(numParticipants) - numParticipants
    private val numConcurrentMatches = numConcurrentMatches
    var currentMatches = ArrayDeque<Match>(numConcurrentMatches)

    private fun getNextEmptyParticipantLO(participantNode: ParticipantNode): ParticipantNode? {
        val queue = ArrayDeque<ParticipantNode>(numParticipants)

        queue.addLast(participantNode)

        while (queue.size > 0) {
            val temp = queue.removeFirst()

            if (temp.hasLeft) {
                queue.addLast(temp.left!!)
                if (temp.hasRight) {
                    queue.addLast(temp.right!!)
                    if ((! temp.left!!.participant.isEmpty) && (! temp.right!!.participant.isEmpty) && (! temp.hasLinkedMatch)) {
                        return temp
                    }
                }
            }
        }

        return null
    }

    public fun getNextMatches(): List<Match> {
        if (currentMatches.size < numConcurrentMatches) {
            val matchesToMake = (numConcurrentMatches - currentMatches.size)

            for (i in 0 until matchesToMake) {
                val emptyParticipantNode = getNextEmptyParticipantLO(rootNode)

                emptyParticipantNode?.left?.participant?.let lit@{ left ->
                    emptyParticipantNode.right?.participant?.let { right ->
                         if (left.isEmpty || right.isEmpty)
                            return@lit

                        val match = Match(left, right)
                        emptyParticipantNode.linkedMatch = match
                        currentMatches.addFirst(match)
                    }
                }
            }
        }

        return currentMatches.toList()
    }

    init {
        require(initialParticipants.size > 1) {
            "The amount of participants in the tournament must be greater than 1"
        }

        require(numConcurrentMatches > 0) {
            "The amount of matches that can exist at once must be greater than 0"
        }

        val participants = ArrayDeque<ParticipantNode>(numParticipants)

        for (i in 0 until numParticipants) {
            participants.addLast(ParticipantNode(initialParticipants[i]))
        }

        var i: Int = 0
        while (participants.size > 1) {
            i++
            val first = participants.removeFirst()
            val second = participants.removeFirst()

            val temp = ParticipantNode()
            temp.right = first
            temp.left = second

            participants.addLast(temp)
        }

        rootNode = participants.removeFirst()
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