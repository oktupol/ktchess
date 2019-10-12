package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player

class Pawn(player: Player, board: Board) : AbstractPiece(player, board) {
	private var moved = false
	private var justMovedDouble = false

	companion object Flags {
		const val MOVED_DOUBLE = "pawn.flag.movedDouble"
		const val REACHED_LAST_ROW = "pawn.flag.reachedLastRow"
	}

	override fun getMovesWithoutCheckTests(): MutableSet<Move> {
		val moves = HashSet<Move>()

		addStraightMoves(moves)
		addDiagonalMoves(moves)

		return moves
	}

	private fun addStraightMoves(moves: MutableSet<Move>) {
		val maxDistance = when (moved) {
			false -> 2
			true -> 1
		}

		for (distance in 1..maxDistance) {
			val targetY = coordinates.y + player.direction * distance

			if (targetY !in 0..7) break
			val targetCoordinates = Coordinates(coordinates.x, targetY)

			if (board.getPieceAt(targetCoordinates) != null) break

			moves.add(Move(this, targetCoordinates, null, createFlags(distance = distance, targetY = targetY)))
		}
	}

	private fun addDiagonalMoves(moves: MutableSet<Move>) {
		for (deviation in intArrayOf(-1, 1)) {
			val targetX = coordinates.x + deviation
			val targetY = coordinates.y + player.direction

			if (targetX !in 0..7 || targetY !in 0..7) break
			val targetCoordinates = Coordinates(targetX, targetY)

			addRegularCapture(targetCoordinates, moves)
			addEnPassantCapture(targetCoordinates, Coordinates(targetX, coordinates.y), moves)
		}
	}

	private fun addRegularCapture(targetCoordinates: Coordinates, moves: MutableSet<Move>) {
		val capturedPiece = board.getPieceAt(targetCoordinates) ?: return
		if (capturedPiece.player != player.opponent()) return
		moves.add(Move(this, targetCoordinates, capturedPiece, createFlags(targetY = targetCoordinates.y)))
	}

	private fun addEnPassantCapture(
		targetCoordinates: Coordinates,
		passedCoordinates: Coordinates,
		moves: MutableSet<Move>
	) {
		val capturedPiece = board.getPieceAt(passedCoordinates) ?: return
		if (capturedPiece.player != player.opponent()) return
		if (capturedPiece !is Pawn) return
		if (!capturedPiece.justMovedDouble) return
		moves.add(Move(this, targetCoordinates, capturedPiece, createFlags(targetY = targetCoordinates.y)))
	}

	private fun createFlags(distance: Int? = null, targetY: Int? = null): Map<String, Boolean> {
		val flags = HashMap<String, Boolean>()
		if (distance == 2) flags[MOVED_DOUBLE] = true
		if (targetY == player.opponent().baseRow) flags[REACHED_LAST_ROW] = true

		return flags
	}

	override fun move(coordinates: Coordinates): Move {
		val move = super.move(coordinates)

		justMovedDouble = move.has(MOVED_DOUBLE)
		moved = true

		// todo promotion

		return move
	}
}