package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player

interface Piece {
    val player: Player
    val board: Board

    fun getMoves(): Set<Move>

    fun move(move: Move)
}