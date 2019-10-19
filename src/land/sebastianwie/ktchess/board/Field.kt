package land.sebastianwie.ktchess.board

import land.sebastianwie.ktchess.game.Player
import land.sebastianwie.ktchess.piece.Piece
import java.util.*

class Field(val coordinates: Coordinates, private val board: Board) {
	var piece: Piece? = null

	val leftNeighbour: Field?
		get() = getFieldAt(coordinates.x - 1, coordinates.y)

	val rightNeighbour: Field?
		get() = getFieldAt(coordinates.x + 1, coordinates.y)

	val upperNeighbour: Field?
		get() = getFieldAt(coordinates.x, coordinates.y - 1)

	val lowerNeighbour: Field?
		get() = getFieldAt(coordinates.x, coordinates.y + 1)

	private fun getFieldAt(x: Int, y: Int): Field? = if (x in 0..7 && y in 0..7) board.getFieldAt(x, y) else null

	fun isBaseRow(player: Player) = player.baseRow == coordinates.y
	fun isLastRow(player: Player) = player.opponent().baseRow == coordinates.y

	override fun hashCode(): Int {
		return Objects.hash(coordinates, piece)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Field

		if (coordinates != other.coordinates) return false
		if (piece != other.piece) return false

		return true
	}
}