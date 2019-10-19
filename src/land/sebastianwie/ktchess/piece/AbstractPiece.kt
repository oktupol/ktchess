package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player
import java.util.*
import kotlin.reflect.jvm.jvmName

abstract class AbstractPiece(override val player: Player, override val board: Board) : Piece {
	override val coordinates: Coordinates
		get() = board.findPiece(this).coordinates

	override fun getMoves(): Set<Move> {
		val moves = getMovesWithoutCheckTests()

		moves.removeIf { move -> board.simulate(move).playerInCheck }

		return moves
	}

	private fun getMoveByCoordinates(coordinates: Coordinates): Move {
		for (move in getMoves())
			if (move.coordinates == coordinates)
				return move

		throw IllegalArgumentException("This move is illegal")
	}

	override fun move(coordinates: Coordinates): Move {
		val move = getMoveByCoordinates(coordinates)

		move(move)

		return move
	}

	override fun move(move: Move) {
		require(move.piece == this)

		if (move.capturedPiece != null) {
			board.deletePiece(move.capturedPiece)
		}

		board.deletePiece(this)
		board.setPieceAt(move.coordinates, this)
	}

	override fun hashCode(): Int {
		return Objects.hash(player.name, this::class.jvmName)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is AbstractPiece) return false

		if (player != other.player) return false
		if (this::class.jvmName != other::class.jvmName) return false
		if (this.coordinates != other.coordinates) return false

		return true
	}
}