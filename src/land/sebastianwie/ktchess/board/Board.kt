package land.sebastianwie.ktchess.board

import land.sebastianwie.ktchess.piece.Piece

class Board {
    private val fields = Array(8) { i -> Array(8) { j -> Field(Coordinates(i, j), this) } }

    fun getFieldAt(x: Int, y: Int) = getFieldAt(Coordinates(x, y))
    fun getFieldAt(coordinates: Coordinates) = fields[coordinates.x][coordinates.y]

    fun getPieceAt(x: Int, y: Int) = getPieceAt(Coordinates(x, y))
    fun getPieceAt(coordinates: Coordinates) = getFieldAt(coordinates).piece

    fun setPieceAt(x: Int, y: Int, piece: Piece) {
        setPieceAt(Coordinates(x, y), piece)
    }

    fun setPieceAt(coordinates: Coordinates, piece: Piece) {
        getFieldAt(coordinates).piece = piece
    }

    fun deletePieceAt(x: Int, y: Int) {
        deletePieceAt(Coordinates(x, y))
    }

    fun deletePieceAt(coordinates: Coordinates) {
        getFieldAt(coordinates).piece = null
    }

    fun findPiece(piece: Piece): Field {
        for (row in fields)
            for (field in row)
                if (field.piece === piece) return field

        throw Exception("Piece not found on board")
    }

    fun deletePiece(piece: Piece) {
        findPiece(piece).piece = null
    }
}