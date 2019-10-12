package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player

interface Piece {
	val coordinates: Coordinates
	val player: Player
	val board: Board

	fun getMovesWithoutCheckTests(): MutableSet<Move>
	fun getMoves(): Set<Move>

	fun move(coordinates: Coordinates): Move
	fun move(move: Move)
}