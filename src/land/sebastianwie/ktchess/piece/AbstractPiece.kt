package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player

abstract class AbstractPiece(override val player: Player, override val board: Board) : Piece {
    val coordinates: Coordinates
        get() = board.findPiece(this).coordinates

    override fun move(move: Move) {
        require(getMoves().contains(move)) { "This move is illegal." }

        if (move.capturedPiece != null) {
            // TODO add captured piece to score board
            board.deletePiece(move.capturedPiece)
        }

        board.deletePiece(this)
        board.setPieceAt(move.coordinates, this)
    }
}