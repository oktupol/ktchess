package land.sebastianwie.ktchess.game

import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.piece.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class GameTest {
    lateinit var game: Game

    @BeforeEach
    fun setUp() {
        game = Game(false)
    }

    @Test
    fun testConstruct() {
        game = Game(true)

        for (y in intArrayOf(0, 7)) {
            assertTrue(game.board.getPieceAt(0, y) is Rook)
            assertTrue(game.board.getPieceAt(1, y) is Knight)
            assertTrue(game.board.getPieceAt(2, y) is Bishop)
            assertTrue(game.board.getPieceAt(3, y) is Queen)
            assertTrue(game.board.getPieceAt(4, y) is King)
            assertTrue(game.board.getPieceAt(5, y) is Bishop)
            assertTrue(game.board.getPieceAt(6, y) is Knight)
            assertTrue(game.board.getPieceAt(7, y) is Rook)
        }

        for (x in 0..7) {
            for (y in intArrayOf(1, 6))
                assertTrue(game.board.getPieceAt(x, y) is Pawn)

            assertEquals(Player.BLACK, game.board.getPieceAt(x, 0)!!.player)
            assertEquals(Player.BLACK, game.board.getPieceAt(x, 1)!!.player)
            assertEquals(Player.WHITE, game.board.getPieceAt(x, 6)!!.player)
            assertEquals(Player.WHITE, game.board.getPieceAt(x, 7)!!.player)

            for (y in 2..5)
                assertNull(game.board.getPieceAt(x, y))
        }
    }
}