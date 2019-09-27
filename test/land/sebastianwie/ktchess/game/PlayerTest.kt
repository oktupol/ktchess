package land.sebastianwie.ktchess.game

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlayerTest{

    @Test
    fun testDirection() {
        assertEquals(-1, Player.WHITE.direction)
        assertEquals(1, Player.BLACK.direction)
    }

    @Test
    fun testOpponent() {
        assertEquals(Player.BLACK, Player.WHITE.opponent())
        assertEquals(Player.WHITE, Player.BLACK.opponent())
    }
}