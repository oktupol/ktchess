package land.sebastianwie.ktchess.board

import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player
import land.sebastianwie.ktchess.piece.King
import land.sebastianwie.ktchess.piece.Piece
import kotlin.reflect.full.primaryConstructor

class Board {
    internal val fields = Array(8) { y -> Array(8) { x -> Field(Coordinates(x, y), this) } }

    fun getFieldAt(x: Int, y: Int) = getFieldAt(Coordinates(x, y))
    fun getFieldAt(coordinates: Coordinates) = fields[coordinates.y][coordinates.x]

    fun getPieceAt(x: Int, y: Int) = getPieceAt(Coordinates(x, y))
    fun getPieceAt(coordinates: Coordinates) = getFieldAt(coordinates).piece

    fun setPieceAt(x: Int, y: Int, piece: Piece) {
        setPieceAt(Coordinates(x, y), piece)
    }

    fun setPieceAt(coordinates: Coordinates, piece: Piece) {
        getFieldAt(coordinates).piece = piece
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

    private fun getPieces(): Set<Piece> {
        val result = HashSet<Piece>()

        for (row in fields) {
            for (field in row) {
                val piece = field.piece ?: continue
                result.add(piece)
            }
        }

        return result
    }

    private fun getPiecesOf(player: Player) = getPieces().filter { piece -> piece.player == player }
    fun getKingOf(player: Player): King? = getPieces().firstOrNull { piece ->
        piece is King && piece.player == player
    } as King?

    fun isCheck(player: Player): Boolean {
        val king = getKingOf(player) ?: return false

        val opponentPieces = getPiecesOf(player.opponent())

        for (opponentPiece in opponentPieces) {
            for (move in opponentPiece.getMovesWithoutCheckTests()) {
                if (move.capturedPiece == king) return true
            }
        }

        return false
    }

    fun isCheckmate(player: Player): Boolean {
        if (!isCheck(player)) return false

        return cannotMove(player)
    }

    fun isStalemate(player: Player): Boolean {
        if (isCheck(player)) return false

        return cannotMove(player)
    }

    private fun cannotMove(player: Player): Boolean {
        val pieces = getPiecesOf(player)

        for (piece in pieces) {
            for (move in piece.getMoves()) {
                if (!simulate(move).playerInCheck) return false
            }
        }

        return true
    }

    fun clone(): Board {
        val clone = Board()

        for ((y, row) in fields.withIndex()) {
            for ((x, field) in row.withIndex()) {
                val piece = field.piece ?: continue
                val clonedPiece = piece::class.primaryConstructor!!.call(piece.player, clone)
                clone.setPieceAt(x, y, clonedPiece)
            }
        }

        return clone
    }

    data class SimulationResult(
        val playerInCheck: Boolean,
        val opponentInCheck: Boolean
    )

    fun simulate(move: Move): SimulationResult {
        val clone = clone()
        val clonedPiece = clone.getPieceAt(move.piece.coordinates)!!

        val capturedPieceCoordinates = move.capturedPiece?.coordinates
        val clonedCapturedPiece =
            if (capturedPieceCoordinates != null) clone.getPieceAt(capturedPieceCoordinates)!!
            else null

        val clonedMove = Move(clonedPiece, move.coordinates, clonedCapturedPiece, move.flags)

        clonedPiece.move(clonedMove)

        return SimulationResult(
            playerInCheck = clone.isCheck(move.piece.player),
            opponentInCheck = clone.isCheck(move.piece.player.opponent())
        )
    }
}