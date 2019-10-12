package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Player
import land.sebastianwie.ktchess.testutils.show
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PawnTest : AbstractPieceTest<Pawn>() {
    @BeforeEach
    fun setUp() {
        board = Board()
        piece = Pawn(Player.BLACK, board)
    }

    @Test
    fun testGetMoves_initialPosition() {
        val expectation = ("" +
                "........|" +
                "..P.....|" +
                "..x.....|" +
                "..x.....|" +
                "........|" +
                "........|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(2, 1, piece)

        assertEquals(expectation, board.show(piece.getMoves()))
    }

    @Test
    fun testGetMoves_whitePawnInitialPosition() {
        val expectation = ("" +
                "........|" +
                "........|" +
                "........|" +
                "........|" +
                "..x.....|" +
                "..x.....|" +
                "..p.....|" +
                "........").replace('|', '\n')

        val whitePawn = Pawn(Player.WHITE, board)
        board.setPieceAt(2, 6, whitePawn)

        assertEquals(expectation, board.show(whitePawn.getMoves()))
    }

    @Test
    fun testGetMoves_afterOneMove() {
        val expectation = ("" +
                "........|" +
                "........|" +
                "..P.....|" +
                "..x.....|" +
                "........|" +
                "........|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(2, 1, piece)
        piece.move(Coordinates(2, 2))

        assertEquals(expectation, board.show(piece.getMoves()))
    }

    @Test
    fun testGetMoves_opponentInFront() {
        val expectation = ("" +
                "........|" +
                "........|" +
                "..P.....|" +
                "..p.....|" +
                "........|" +
                "........|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(2, 1, piece)
        board.setPieceAt(2, 3, Pawn(Player.WHITE, board))
        piece.move(Coordinates(2, 2))

        assertEquals(expectation, board.show(piece.getMoves()))
    }

    @Test
    fun testGetMoves_initialPositionAndOpponentDiagonallyAhead() {
        val expectation = ("" +
                "........|" +
                "..P.....|" +
                "..xX....|" +
                "..x.....|" +
                "........|" +
                "........|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(2, 1, piece)
        board.setPieceAt(3, 2, Pawn(Player.WHITE, board))

        assertEquals(expectation, board.show(piece.getMoves()))
    }

    @Test
    fun testGetMoves_nextToOpponentPawn() {
        val expectation = ("" +
                "........|" +
                "........|" +
                "........|" +
                "........|" +
                "...Pp...|" +
                "...x....|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(3, 3, piece)
        piece.move(Coordinates(3, 4))

        board.setPieceAt(4, 4, Pawn(Player.WHITE, board))

        assertEquals(expectation, board.show(piece.getMoves()))
    }

    @Test
    fun testGetMoves_nextToOpponentPawnThatMovedDouble() {
        val expectation = ("" +
                "........|" +
                "........|" +
                "........|" +
                "........|" +
                "...Pp...|" +
                "...xX...|" + // en passant move - the white pawn would be captured
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(3, 3, piece)
        piece.move(Coordinates(3, 4))

        val opponentPawn = Pawn(Player.WHITE, board)
        board.setPieceAt(4, 6, opponentPawn)
        opponentPawn.move(Coordinates(4, 4))

        assertEquals(expectation, board.show(piece.getMoves()))
    }

    @Test
    fun testGetMoves_nextToOpponentPawnThatMovedDoubleMoreThanOneMoveAgo() {
        val expectation = ("" +
                "........|" +
                "........|" +
                "........|" +
                "...Pp...|" +
                "...x....|" +
                "........|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(3, 2, piece)
        val opponentPawn = Pawn(Player.WHITE, board)
        board.setPieceAt(4, 6, opponentPawn)

        opponentPawn.move(Coordinates(4, 4))
        piece.move(Coordinates(3, 3))
        opponentPawn.move(Coordinates(4, 3))


        assertEquals(expectation, board.show(piece.getMoves()))
    }

    @Test
    fun testGetMoves_kingInCheck_mustCapturePiece() {
        val expectation = ("" +
                "...K....|" +
                "....P...|" +
                "...X....|" +
                "........|" +
                "........|" +
                "...q....|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(4, 1, piece)
        board.setPieceAt(3, 0, King(Player.BLACK, board))
        board.setPieceAt(3, 2, Rook(Player.WHITE, board))
        board.setPieceAt(3, 5, Queen(Player.WHITE, board))

        assertEquals(expectation, board.show(piece.getMoves()))
    }

    @Test
    fun testGetMoves_kingInCheck_mustMoveDouble() {
        val expectation = ("" +
                "........|" +
                "..P.....|" +
                "...p....|" +
                "K.x..r..|" +
                "........|" +
                "........|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(2, 1, piece)
        board.setPieceAt(3, 2, Pawn(Player.WHITE, board))
        board.setPieceAt(0, 3, King(Player.BLACK, board))
        board.setPieceAt(5, 3, Rook(Player.WHITE, board))

        assertEquals(expectation, board.show(piece.getMoves()))
    }

    @Test
    fun testGetMoves_kingInCheck_cannotMove() {
        val expectation = ("" +
                "........|" +
                "..P.....|" +
                "...p....|" +
                "........|" +
                "........|" +
                ".K....r.|" +
                "........|" +
                "........").replace('|', '\n')

        board.setPieceAt(2, 1, piece)
        board.setPieceAt(3, 2, Pawn(Player.WHITE, board))
        board.setPieceAt(1, 5, King(Player.BLACK, board))
        board.setPieceAt(6, 5, Rook(Player.WHITE, board))

        assertEquals(expectation, board.show(piece.getMoves()))
    }
}