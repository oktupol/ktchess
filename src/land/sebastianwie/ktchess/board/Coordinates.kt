package land.sebastianwie.ktchess.board

data class Coordinates(val x: Int, val y: Int) {
	init {
		require(x in 0..7) { "x must be between 0 and 7" }
		require(y in 0..7) { "y must be between 0 and 7" }
	}

	override fun toString(): String {
		return "" + (x.toChar() + 'A'.toInt()) + (y.toChar() + '1'.toInt())
	}

	companion object {
		fun fromString(s: String): Coordinates {
			require(s.length == 2)

			val chars = s.toUpperCase().toCharArray()

			val xChar = chars[0]
			val yChar = chars[1]

			val x = xChar - 'A'
			val y = yChar - '1'

			return Coordinates(x, y)
		}
	}
}