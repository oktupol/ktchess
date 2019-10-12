package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player

class Rook(player: Player, board: Board) : SimpleMovesPiece(player, board) {
	override val canMoveDiagonally = false
	override val canMoveOrthogonally = true

	internal var moved = false

	override fun move(coordinates: Coordinates): Move {
		val move = super.move(coordinates)

		moved = true

		return move
	}
}