package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.game.Player

class Rook(player: Player, board: Board): SimpleMovesPiece(player, board) {
    override val canMoveDiagonally = false
    override val canMoveOrthogonally = true
}