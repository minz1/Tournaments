class ParticipantNode constructor(participant: Participant = Participant()) {
    val participant = participant
    var linkedMatch: Match? = null

    var left: ParticipantNode? = null
    var right: ParticipantNode? = null

    val hasLeft: Boolean
        get() = left != null

    val hasRight: Boolean
        get() = right != null

    val hasLinkedMatch: Boolean
        get() = linkedMatch != null
}