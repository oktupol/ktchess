package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.game.Player
import land.sebastianwie.ktchess.testutils.show
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class KnightTest : AbstractPieceTest<Knight>() {

	@BeforeEach
	fun setUp() {
		board = Board()
		piece = Knight(Player.BLACK, board)
	}

	@Test
	fun testGetMoves() {
		val expectation = ("" +
				"........|" +
				"........|" +
				"...x.x..|" +
				"..x...x.|" +
				"....N...|" +
				"..x...x.|" +
				"...x.x..|" +
				"........").replace('|', '\n')

		board.setPieceAt(4, 4, piece)

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun getMovesWhileCloseToCorner() {
		val expectation = ("" +
				"....x...|" +
				"......N.|" +
				"....x...|" +
				".....x.x|" +
				"........|" +
				"........|" +
				"........|" +
				"........").replace('|', '\n')

		board.setPieceAt(6, 1, piece)

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun getMovesWithOtherPiecesOccupyingFields() {
		val expectation = ("" +
				"........|" +
				"........|" +
				"...X.x..|" +
				"..x...P.|" +
				"....N...|" +
				"..P...x.|" +
				"...x.X..|" +
				"........").replace('|', '\n')

		board.setPieceAt(4, 4, piece)

		board.setPieceAt(2, 5, Pawn(Player.BLACK, board))
		board.setPieceAt(5, 6, Pawn(Player.WHITE, board))
		board.setPieceAt(6, 3, Pawn(Player.BLACK, board))
		board.setPieceAt(3, 2, Pawn(Player.WHITE, board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun getMovesWithKingInCheck_moveBetweenOpponentAndKing() {
		val expectation = ("" +
				"........|" +
				"........|" +
				"........|" +
				"........|" +
				"..r.N...|" +
				"..x.....|" +
				"........|" +
				"..K.....").replace('|', '\n')

		board.setPieceAt(4, 4, piece)
		board.setPieceAt(2, 4, Rook(Player.WHITE, board))
		board.setPieceAt(2, 7, King(Player.BLACK, board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun getMovesWithKingInCheck_capturePiece() {
		val expectation = ("" +
				"........|" +
				".....N..|" +
				"........|" +
				"......X.|" +
				"........|" +
				"........|" +
				"........|" +
				"..K.....").replace('|', '\n')

		board.setPieceAt(5, 1, piece)
		board.setPieceAt(6, 3, Bishop(Player.WHITE, board))
		board.setPieceAt(2, 7, King(Player.BLACK, board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun getMovesWithKingInCheck_capturePieceOrMoveInBetween() {
		val expectation = ("" +
				"........|" +
				"........|" +
				"....N...|" +
				"......X.|" +
				".....x..|" +
				"........|" +
				"........|" +
				"..K.....").replace('|', '\n')

		board.setPieceAt(4, 2, piece)
		board.setPieceAt(6, 3, Bishop(Player.WHITE, board))
		board.setPieceAt(2, 7, King(Player.BLACK, board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun getMovesWithKingInCheck_noPossibleMoves() {
		val expectation = ("" +
				"........|" +
				"........|" +
				"....N...|" +
				"........|" +
				"........|" +
				"........|" +
				"........|" +
				".rK.....").replace('|', '\n')

		board.setPieceAt(4, 2, piece)
		board.setPieceAt(1, 7, Rook(Player.WHITE, board))
		board.setPieceAt(2, 7, King(Player.BLACK, board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}
}