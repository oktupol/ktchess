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
        val moves = super.getMovesWithoutCheckTests()

        addCastleMoves(moves)

        return moves
    }

    private fun addCastleMoves(moves: MutableSet<Move>) {
        if (moved) return
        if (board.isCheck(player)) return

        castlingOptions@ for (castlingOption in CastlingOption.values()) {
            val rook = board.getPieceAt(castlingOption.x, player.baseRow) ?: continue
            if (rook !is Rook) continue
            if (rook.player != player) continue
            if (rook.moved) continue

            val simulation = board.clone()
            val king = simulation.getKingOf(player)!!

            for (interimY in castlingOption.betweenSpaceStart..castlingOption.betweenSpaceEnd) {
                if (board.getPieceAt(interimY, player.baseRow) != null) continue@castlingOptions

                simulation.deletePiece(king)
                simulation.setPieceAt(interimY, player.baseRow, king)

                if (simulation.isCheck(player)) continue@castlingOptions
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