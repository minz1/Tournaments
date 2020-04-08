class MatchNode constructor(match: Match = Match()) {
    val match = match

    var left: MatchNode? = null
    var right: MatchNode? = null

    val hasLeft: Boolean
        get() = left != null

    val hasRight: Boolean
        get() = right != null
}