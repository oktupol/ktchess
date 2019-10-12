package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Player
import land.sebastianwie.ktchess.testutils.show
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class KingTest : AbstractPieceTest<King>() {
	@BeforeEach
	fun setUp() {
		board = Board()
		piece = King(Player.BLACK, board)
	}

	@Test
	fun testGetMoves() {
		val expectation = ("" +
			"........|" +
			"........|" +
			"...xxx..|" +
			"...xKx..|" +
			"...xxx..|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 3, piece)

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesInCorner() {
		val expectation = ("" +
			"......xK|" +
			"......xx|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(7, 0, piece)

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithOpponent() {
		val expectation = ("" +
			"........|" +
			"........|" +
			".b.xX...|" +
			"...xK...|" +
			"....x...|" +
			"........|" +
			".....r..|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 3, piece)
		board.setPieceAt(5, 6, Rook(piece.player.opponent(), board))
		board.setPieceAt(1, 2, Bishop(piece.player.opponent(), board))
		board.setPieceAt(4, 2, Knight(piece.player.opponent(), board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithKingsideCastle() {
		val expectation = ("" +
			"...xKxxR|" +
			"...xxx..|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 0, piece)
		board.setPieceAt(7, 0, Rook(piece.player, board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithQueensideCastle() {
		val expectation = ("" +
			"R.xxKx..|" +
			"...xxx..|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 0, piece)
		board.setPieceAt(0, 0, Rook(piece.player, board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithBothSidesCastle() {
		val expectation = ("" +
			"R.xxKxxR|" +
			"...xxx..|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 0, piece)
		board.setPieceAt(0, 0, Rook(piece.player, board))
		board.setPieceAt(7, 0, Rook(piece.player, board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithBothSidesCastle_kingSideRookMoved() {
		val expectation = ("" +
			"R.xxKx.R|" +
			"...xxx..|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 0, piece)
		val kingsideRook = Rook(piece.player, board)
		board.setPieceAt(0, 0, Rook(piece.player, board))
		board.setPieceAt(7, 0, kingsideRook)

		kingsideRook.move(Coordinates(7, 1))
		kingsideRook.move(Coordinates(7, 0))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithBothSidesCastle_queenSideRookMoved() {
		val expectation = ("" +
			"R..xKxxR|" +
			"...xxx..|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 0, piece)
		val queensideRook = Rook(piece.player, board)
		board.setPieceAt(0, 0, queensideRook)
		board.setPieceAt(7, 0, Rook(piece.player, board))

		queensideRook.move(Coordinates(0, 1))
		queensideRook.move(Coordinates(0, 0))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithBothSidesCastle_kingMoved() {
		val expectation = ("" +
			"R..xKx.R|" +
			"...xxx..|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 0, piece)
		board.setPieceAt(0, 0, Rook(piece.player, board))
		board.setPieceAt(7, 0, Rook(piece.player, board))

		piece.move(Coordinates(3, 0))
		piece.move(Coordinates(4, 0))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithCastle_castleBlocked() {
		val expectation = ("" +
			"RP.xKx..|" +
			"...xxx..|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 0, piece)
		board.setPieceAt(0, 0, Rook(piece.player, board))
		board.setPieceAt(1, 0, Pawn(piece.player, board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithCastle_kingInCheck() {
		val expectation = ("" +
			"R..xKx.R|" +
			"...x.x..|" +
			"........|" +
			"....r...|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 0, piece)
		board.setPieceAt(0, 0, Rook(piece.player, board))
		board.setPieceAt(7, 0, Rook(piece.player, board))
		board.setPieceAt(4, 3, Rook(piece.player.opponent(), board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}

	@Test
	fun testGetMovesWithCastle_kingWouldMoveThroughCheck() {
		val expectation = ("" +
			"R...KxxR|" +
			"....xx..|" +
			"........|" +
			"...r....|" +
			"........|" +
			"........|" +
			"........|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 0, piece)
		board.setPieceAt(0, 0, Rook(piece.player, board))
		board.setPieceAt(7, 0, Rook(piece.player, board))
		board.setPieceAt(3, 3, Rook(piece.player.opponent(), board))

		assertEquals(expectation, board.show(piece.getMoves()))
	}
}