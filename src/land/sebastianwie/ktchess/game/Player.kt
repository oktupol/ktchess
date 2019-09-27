package land.sebastianwie.ktchess.game

/**
 * White player moves "up", towards negative y.
 * Black player moves "down", towards positive y.
 */
enum class Player(val direction: Int, val baseRow: Int) {
    WHITE(-1, 7),
    BLACK(1, 0);

    fun opponent() = if (this == BLACK) WHITE else BLACK
}