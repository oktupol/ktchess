package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player

class Knight(player: Player, board: Board) : AbstractPiece(player, board), PromotionOptionPiece {
	override fun getMovesWithoutCheckTests(): MutableSet<Move> {
		val moves = HashSet<Move>()

		val patterns = arrayOf(intArrayOf(2, 1), intArrayOf(1, 2))
		for (pattern in patterns) {
			for (xDirection in intArrayOf(-1, 1)) {
				for (yDirection in intArrayOf(-1, 1)) {
					val dx = pattern[0] * xDirection
					val dy = pattern[1] * yDirection

					val targetX = coordinates.x + dx
					val targetY = coordinates.y + dy

					if (targetX !in 0..7 || targetY !in 0..7) continue

					val targetCoordinates = Coordinates(targetX, targetY)

					val capturedPiece = board.getPieceAt(targetCoordinates)
					if (capturedPiece != null && capturedPiece.player == player) continue

					moves.add(Move(this, targetCoordinates, capturedPiece))
				}
			}
		}

		return moves
	}
}