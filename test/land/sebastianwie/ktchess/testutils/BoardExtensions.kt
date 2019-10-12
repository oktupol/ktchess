package land.sebastianwie.ktchess.testutils

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player
import land.sebastianwie.ktchess.piece.*

fun getRepresentation(piece: Piece?): Char {
	if (piece == null) return '.'

	val character = when (piece::class) {
		Pawn::class -> 'p'
		Rook::class -> 'r'
		Knight::class -> 'n'
		Bishop::class -> 'b'
		Queen::class -> 'q'
		King::class -> 'k'
		else -> '.'
	}

	return if (piece.player == Player.WHITE) character
	else character.toUpperCase()
}

fun getPiece(character: Char, board: Board): Piece? {
	val player = if (character.isUpperCase()) Player.BLACK
	else Player.WHITE

	return when (character.toLowerCase()) {
		'p' -> Pawn(player, board)
		'r' -> Rook(player, board)
		'n' -> Knight(player, board)
		'b' -> Bishop(player, board)
		'q' -> Queen(player, board)
		'k' -> King(player, board)
		else -> null
	}
}

fun Board.show(moves: Set<Move>? = null): String {
	val result = StringBuilder()

	val moveCoords = HashSet<Coordinates>()
	val captureCoords = HashSet<Coordinates>()

	if (moves != null) {
		moveCoords.addAll(moves.filter { move -> move.capturedPiece == null }.map { move -> move.coordinates })
		captureCoords.addAll(moves.filter { move -> move.capturedPiece != null }.map { move -> move.coordinates })
	}

	for (row in fields) {
		for (field in row) {
			result.append(
				when {
					captureCoords.contains(field.coordinates) -> 'X'
					moveCoords.contains(field.coordinates) -> 'x'
					else -> getRepresentation(field.piece)
				}
			)
		}
		result.append('\n')
	}

	return result.toString().trim()
}

fun Board.load(string: String) {
	val rows = string.split("\n")

	for ((y, row) in rows.withIndex()) {
		for ((x, character) in row.toCharArray().withIndex()) {
			getFieldAt(x, y).piece = getPiece(character, this)
		}
	}
}