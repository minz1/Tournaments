class TournamentTree(numParticipants: Int) {
    lateinit var rootNode: ParticipantNode
    val numParticipants = numParticipants
    val size: Int = (2 * numParticipants) - 1
    val byes: Int
        get() = getNextPowerOf2(size) - size

    init {
        require(numParticipants > 1) {
            "A tournament must have more than 1 match."
        }

        val participants = ArrayDeque<ParticipantNode>(numParticipants)
        for (i in 0 until numParticipants) {
            participants.addLast(ParticipantNode())
        }

        while (participants.size > 1) {
            val first = participants.removeFirst()
            val second = participants.removeFirst()

            val temp = ParticipantNode()
            temp.left = first
            temp.right = second

            participants.addLast(temp)
        }

        rootNode = participants.removeFirst()
    }

    fun getNextPowerOf2(number: Int): Int {
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