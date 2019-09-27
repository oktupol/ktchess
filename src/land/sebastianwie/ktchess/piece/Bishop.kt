package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.game.Player

class Bishop(player: Player, board: Board): SimpleMovesPiece(player, board) {
    override val canMoveDiagonally = true
    override val canMoveOrthogonally = false
}