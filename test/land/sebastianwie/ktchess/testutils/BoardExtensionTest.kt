package land.sebastianwie.ktchess.testutils

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Player
import land.sebastianwie.ktchess.piece.Bishop
import land.sebastianwie.ktchess.piece.Pawn
import land.sebastianwie.ktchess.piece.Rook
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class BoardExtensionTest {
	lateinit var board: Board

	@BeforeEach
	fun setUp() {
		board = Board()
	}

	@Test
	fun testShow() {
		val expectation = ("" +
			"........|" +
			"........|" +
			"........|" +
			"..p.....|" +
			"........|" +
			"........|" +
			"....P...|" +
			"........").replace('|', '\n')
		board.getFieldAt(2, 3).piece = Pawn(Player.WHITE, board)
		board.getFieldAt(4, 6).piece = Pawn(Player.BLACK, board)

		assertEquals(expectation, board.show())
	}

	@Test
	fun testShowWithMoves() {
		val expectation = ("" +
			"....x...|" +
			"....x...|" +
			"....x...|" +
			"..Xxrxxx|" +
			"....x...|" +
			"....x...|" +
			"....p...|" +
			"........").replace('|', '\n')

		board.setPieceAt(4, 3, Rook(Player.WHITE, board))
		board.setPieceAt(4, 6, Pawn(Player.WHITE, board))
		board.setPieceAt(2, 3, Pawn(Player.BLACK, board))

		assertEquals(expectation, board.show(board.getPieceAt(4, 3)!!.getMoves()))
	}

	@Test
	fun testLoad() {
		val pattern = ("" +
			"RNBQKBNR|" +
			"PPPPPPPP|" +
			"........|" +
			"........|" +
			"........|" +
			"........|" +
			"pppppppp|" +
			"rnbqkbnr").replace('|', '\n')

		board.load(pattern)

		assertEquals(pattern, board.show())
		assertTrue(board.getPieceAt(Coordinates(2, 0))!! is Bishop)
		assertEquals(Player.BLACK, board.getPieceAt(3, 1)!!.player)
		assertEquals(Player.WHITE, board.getPieceAt(6, 7)!!.player)
	}
}