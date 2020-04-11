data class Participant (val name: String = "", var seed: Int = -1) {
    val isEmpty: Boolean
        get() = name.isNullOrEmpty()
}