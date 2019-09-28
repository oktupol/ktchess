package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Player
import land.sebastianwie.ktchess.testutils.show
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class KingTest {
    lateinit var board: Board
    lateinit var king: King

    @BeforeEach
    fun setUp() {
        board = Board()
        king = King(Player.BLACK, board)
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

        board.setPieceAt(4, 3, king)

        assertEquals(expectation, board.show(king.getMoves()))
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

        board.setPieceAt(7, 0, king)

        assertEquals(expectation, board.show(king.getMoves()))
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

        board.setPieceAt(4, 3, king)
        board.setPieceAt(5, 6, Rook(king.player.opponent(), board))
        board.setPieceAt(1, 2, Bishop(king.player.opponent(), board))
        board.setPieceAt(4, 2, Knight(king.player.opponent(), board))

        assertEquals(expectation, board.show(king.getMoves()))
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

        board.setPieceAt(4, 0, king)
        board.setPieceAt(7, 0, Rook(king.player, board))

        assertEquals(expectation, board.show(king.getMoves()))
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

        board.setPieceAt(4, 0, king)
        board.setPieceAt(0, 0, Rook(king.player, board))

        assertEquals(expectation, board.show(king.getMoves()))
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

        board.setPieceAt(4, 0, king)
        board.setPieceAt(0, 0, Rook(king.player, board))
        board.setPieceAt(7, 0, Rook(king.player, board))

        assertEquals(expectation, board.show(king.getMoves()))
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

        board.setPieceAt(4, 0, king)
        val kingsideRook = Rook(king.player, board)
        board.setPieceAt(0, 0, Rook(king.player, board))
        board.setPieceAt(7, 0, kingsideRook)

        kingsideRook.move(Coordinates(7, 1))
        kingsideRook.move(Coordinates(7, 0))

        assertEquals(expectation, board.show(king.getMoves()))
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

        board.setPieceAt(4, 0, king)
        val queensideRook = Rook(king.player, board)
        board.setPieceAt(0, 0, queensideRook)
        board.setPieceAt(7, 0, Rook(king.player, board))

        queensideRook.move(Coordinates(0, 1))
        queensideRook.move(Coordinates(0, 0))

        assertEquals(expectation, board.show(king.getMoves()))
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

        board.setPieceAt(4, 0, king)
        board.setPieceAt(0, 0, Rook(king.player, board))
        board.setPieceAt(7, 0, Rook(king.player, board))

        king.move(Coordinates(3, 0))
        king.move(Coordinates(4, 0))

        assertEquals(expectation, board.show(king.getMoves()))
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

        board.setPieceAt(4, 0, king)
        board.setPieceAt(0, 0, Rook(king.player, board))
        board.setPieceAt(1, 0, Pawn(king.player, board))

        assertEquals(expectation, board.show(king.getMoves()))
    }

    @Test
    fun testGetMovesWithCastle_kingInCheck() {
        val expectation = ("" +
                "R..xKx..|" +
                "...x.x..|" +
                "........|" +
                "....r...|" +
                "........|" +
                "........|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(4, 0, king)
        board.setPieceAt(0, 0, Rook(king.player, board))
        board.setPieceAt(4, 3, Rook(king.player.opponent(), board))

        assertEquals(expectation, board.show(king.getMoves()))
    }

    @Test
    fun testGetMovesWithCastle_kingWouldMoveThroughCheck() {
        val expectation = ("" +
                "R...Kx..|" +
                "....xx..|" +
                "........|" +
                "...r....|" +
                "........|" +
                "........|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(4, 0, king)
        board.setPieceAt(0, 0, Rook(king.player, board))
        board.setPieceAt(3, 3, Rook(king.player.opponent(), board))

        assertEquals(expectation, board.show(king.getMoves()))
    }
}