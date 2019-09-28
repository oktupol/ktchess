package land.sebastianwie.ktchess.piece

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.game.Move
import land.sebastianwie.ktchess.game.Player

class King(player: Player, board: Board) : SimpleMovesPiece(player, board) {
    override val canMoveDiagonally = true
    override val canMoveOrthogonally = true
    override val maxMoveDistance = 1

    private var moved = false

    @Suppress("SpellCheckingInspection")
    companion object Flags {
        const val CASTLE_QUEENSIDE = "king.flag.castleQueenside"
        const val CASTLE_KINGSIDE = "king.flag.castleKingside"
    }

    @Suppress("unused", "SpellCheckingInspection")
    enum class CastlingOption(
        val x: Int,
        val dx: Int,
        val rookX: Int,
        val betweenSpaceStart: Int,
        val betweenSpaceEnd: Int,
        val flag: String
    ) {
        QUEENSIDE(0, -2, 3, 1, 3, CASTLE_QUEENSIDE),
        KINGSIDE(7, +2, 5, 5, 6, CASTLE_KINGSIDE)
    }

    override fun getMovesWithoutCheckTests(): MutableSet<Move> {
        val moves = super.getMoves()

        addCastleMoves(moves as MutableSet<Move>)

        return moves
    }

    private fun addCastleMoves(moves: MutableSet<Move>) {
        if (moved) return

        castlingOptions@ for (castlingOption in CastlingOption.values()) {
            val rook = board.getPieceAt(castlingOption.x, player.baseRow) ?: break
            if (rook !is Rook) break
            if (rook.player != player) break
            if (rook.moved) break

            for (interimY in castlingOption.betweenSpaceStart..castlingOption.betweenSpaceEnd) {
                if (board.getPieceAt(interimY, player.baseRow) != null) break@castlingOptions
                // todo check if king is currently not in check or would be in any space between its current and its final position
            }

            val targetCoordinates = Coordinates(coordinates.x + castlingOption.dx, coordinates.y)
            moves.add(Move(this, targetCoordinates, null, castlingOption.flag))
        }
    }

    override fun move(coordinates: Coordinates): Move {
        val move = super.move(coordinates)

        moved = true

        val castlingOption = when {
            move.has(CASTLE_QUEENSIDE) -> CastlingOption.QUEENSIDE
            move.has(CASTLE_KINGSIDE) -> CastlingOption.KINGSIDE
            else -> null
        }

        if (castlingOption != null) {
            val rook = board.getPieceAt(castlingOption.x, player.baseRow)!! as Rook
            board.deletePiece(rook)
            board.setPieceAt(castlingOption.rookX, player.baseRow, rook)
            rook.moved = true
        }

        return move
    }
}