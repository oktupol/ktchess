package land.sebastianwie.ktchess.board

data class Coordinates(val x: Int, val y: Int) {
    init {
        require(x in 0..7) { "x must be between 0 and 7" }
        require(y in 0..7) { "y must be between 0 and 7" }
    }
}