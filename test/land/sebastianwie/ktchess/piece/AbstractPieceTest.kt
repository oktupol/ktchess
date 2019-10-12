package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board

internal abstract class AbstractPieceTest<T : AbstractPiece> {
	lateinit var board: Board
	lateinit var piece: T
}