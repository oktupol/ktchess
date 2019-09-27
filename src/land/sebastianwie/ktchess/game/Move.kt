package land.sebastianwie.ktchess.game

import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.piece.Piece

data class Move(
    val piece: Piece,
    val coordinates: Coordinates,
    val capturedPiece: Piece? = null,
    val flags: Map<String, Boolean> = HashMap()
) {
    constructor(piece: Piece, coordinates: Coordinates, capturedPiece: Piece?, vararg flags: String) : this(
        piece,
        coordinates,
        capturedPiece
    ) {
        val myFlags = this.flags as HashMap
        for (flag in flags) myFlags[flag] = true
    }
}
