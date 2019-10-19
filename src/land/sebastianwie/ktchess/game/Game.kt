package land.sebastianwie.ktchess.game

import land.sebastianwie.ktchess.board.Board
import land.sebastianwie.ktchess.board.Coordinates
import land.sebastianwie.ktchess.piece.*
import kotlin.collections.ArrayList
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

	val history: MutableList<Move>

	val boardHashes: MutableMap<Int, Int>

	// todo handle move flags

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

		history = ArrayList()
		boardHashes = HashMap()
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

	class Selection internal constructor(val moveNumber: Int, val game: Game, val piece: Piece) {
		fun getMoves(): Set<Move> = piece.getMoves()
		fun moveTo(coordinates: Coordinates) {
			require(moveNumber == game.history.size) { "This move is no longer valid" }

			val move = piece.move(coordinates)

			game.history.add(move)
			game.boardHashes[game.board.hashCode()] = game.boardHashes.getOrDefault(game.board.hashCode(), 0) + 1
			// todo event system

			game.endTurn()
		}
	}

	fun select(coordinates: Coordinates): Selection {
		require(gameRunning) { "The game has to be running" }

		val piece = board.getPieceAt(coordinates)
		requireNotNull(piece) { "No piece selected" }
		require(piece.player == currentPlayer) { "Opponent piece selected" }

		return Selection(history.size, this, piece)
	}

	fun fiftyMoveRulePossible(): Boolean {
		if (!gameRunning) return false
		if (history.size < 50) return false

		val iterator = history.listIterator(history.size)
		var i = 0

		while (iterator.hasPrevious() && i < 50) {
			val move = iterator.previous()

			if (move.capturedPiece != null) return false
			if (move.piece is Pawn) return false

			i++
		}

		return true
	}

	fun claimFiftyMoveRule() {
		require(fiftyMoveRulePossible()) { "Cannot claim fifty move rule right now" }

		gameRunning = false
		endTurn()
	}

	fun threefoldRepetitionPossible(): Boolean {
		if (!gameRunning) return false

		for (occurrences in boardHashes) {
			if (occurrences.value >= 3) return true
		}

		return false
	}

	fun claimThreefoldRepetition() {
		require(threefoldRepetitionPossible()) { "Cannot claim threefold repetition right now" }

		gameRunning = false
		endTurn()
	}

	fun surrender() {
		require(gameRunning) { "The game has to be running" }
		endTurn(true)
	}

	data class GameResult internal constructor(val winner: Player?, val draw: Boolean = false)

	internal fun endTurn(surrender: Boolean = false): GameResult? {
		currentPlayer = currentPlayer.opponent()

		if (surrender) {
			gameRunning = false
			return (GameResult(currentPlayer))
		} else if (gameRunning) {
			isCheck = board.isCheck(currentPlayer)
			isCheckmate = board.isCheckmate(currentPlayer)
			isStalemate = board.isStalemate(currentPlayer)

			gameRunning = !isCheckmate && !isStalemate

			if (isCheckmate) return GameResult(currentPlayer.opponent())
			if (isStalemate) return GameResult(null, true)
		} else {
			return GameResult(null, true)
		}

		return null
	}
}