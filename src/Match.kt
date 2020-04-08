class Match constructor(participants: List<Participant> = ArrayList<Participant>()) {
    val participants = participants

    val hasParticipants: Boolean
        get() = participants.isEmpty()

    var isComplete = false
}