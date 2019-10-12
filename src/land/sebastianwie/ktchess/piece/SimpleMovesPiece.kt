package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player

abstract class SimpleMovesPiece(player: Player, board: Board) : AbstractPiece(player, board) {
	abstract val canMoveOrthogonally: Boolean
	abstract val canMoveDiagonally: Boolean
	open val maxMoveDistance = 8


	override fun getMovesWithoutCheckTests(): MutableSet<Move> {
		val moves = HashSet<Move>()

		val directions = Array(3) { i ->
			BooleanArray(3) { j ->
				canMoveOrthogonally && (i == 1 && j != 1 || i != 1 && j == 1)
					|| canMoveDiagonally && i != 1 && j != 1
			}
		}

		for (distance in 1..maxMoveDistance) {
			for (dx in intArrayOf(-distance, 0, distance)) {
				for (dy in intArrayOf(-distance, 0, distance)) {
					if (!directions[sign(dx) + 1][sign(dy) + 1]) continue

					val targetX = this.coordinates.x + dx
					val targetY = this.coordinates.y + dy

					if (targetX !in 0..7 || targetY !in 0..7) continue

					val targetCoords = Coordinates(targetX, targetY)
					val targetField = board.getFieldAt(targetCoords)

					val capturedPiece = targetField.piece

					if (capturedPiece == null) {
						moves.add(Move(this, targetCoords))
						continue
					}

					if (capturedPiece.player == player.opponent()) {
						moves.add(Move(this, targetCoords, capturedPiece))
						directions[sign(dx) + 1][sign(dy) + 1] = false
						continue
					}

					if (capturedPiece.player == player) {
						directions[sign(dx) + 1][sign(dy) + 1] = false
						continue
					}
				}
			}
		}

		return moves
	}

	private fun sign(i: Int) = when {
		i > 0 -> 1
		i < 0 -> -1
		else -> 0
	}
}