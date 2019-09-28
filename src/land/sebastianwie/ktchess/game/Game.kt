package land.sebastianwie.ktchess.game

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.piece.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class Game(init: Boolean = true) {
    val board = Board()

    var currentPlayer = Player.WHITE
        private set

    var gameRunning = true
        private set

    var isCheck = false
        private set

    var isCheckmate = false
        private set

    var isStalemate = false
        private set

    // todo implement scoreboard, threefold-repetition and fifty-move-rule

    private val homeRowTemplate: Array<KClass<out AbstractPiece>> = arrayOf(
        Rook::class,
        Knight::class,
        Bishop::class,
        Queen::class,
        King::class,
        Bishop::class,
        Knight::class,
        Rook::class
    )

    init {
        if (init) {
            initHomeRow(Player.WHITE)
            initHomeRow(Player.BLACK)
            initSecondRow(Player.WHITE)
            initSecondRow(Player.BLACK)
        }
    }

    private fun initHomeRow(player: Player) {
        for ((x, pieceClass) in homeRowTemplate.withIndex()) {
            val piece = pieceClass.primaryConstructor!!.call(player, board)
            board.setPieceAt(x, player.baseRow, piece)
        }
    }

    private fun initSecondRow(player: Player) {
        for (x in 0..7) {
            board.setPieceAt(x, player.baseRow + player.direction, Pawn(player, board))
        }
    }

    class Selection internal constructor(val piece: Piece) {
        fun getMoves(): Set<Move> = piece.getMoves()
        fun moveTo(coordinates: Coordinates) {
            piece.move(coordinates)
        }
    }

    fun select(coordinates: Coordinates): Selection {
        val piece = board.getPieceAt(coordinates)
        requireNotNull(piece) { "No piece selected" }
        require(piece.player == currentPlayer) { "Opponent piece selected" }

        return Selection(piece)
    }

    fun endTurn() {
        currentPlayer = currentPlayer.opponent()

        isCheck = board.isCheck(currentPlayer)
        isCheckmate = board.isCheckmate(currentPlayer)
        isStalemate = board.isStalemate(currentPlayer)

        gameRunning = !isCheckmate && !isStalemate
    }
}