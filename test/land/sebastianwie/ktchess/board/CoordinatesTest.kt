package land.sebastianwie.ktchess.board

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

internal class CoordinatesTest {
    @Test
    fun testCreateLegalCoordinates() {
        val coord1 = Coordinates(0, 0)
        val coord2 = Coordinates(3, 4)
        val coord3 = Coordinates(7, 7)

        assertEquals(0, coord1.x)
        assertEquals(4, coord2.y)
        assertEquals(7, coord3.x)
    }

    @Test
    fun testCreateIllegalCoordinates() {
        assertFailsWith(IllegalArgumentException::class) {
            Coordinates(-1, 0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            Coordinates(0, -1)
        }
        assertFailsWith(IllegalArgumentException::class) {
            Coordinates(8, 3)
        }
        assertFailsWith(IllegalArgumentException::class) {
            Coordinates(6, 8)
        }
    }
}