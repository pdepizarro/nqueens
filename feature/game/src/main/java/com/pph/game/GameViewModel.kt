package com.pph.game

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pph.domain.model.CellPos
import com.pph.domain.model.GameResult
import com.pph.domain.usecases.ObserveBoardNUseCase
import com.pph.domain.usecases.ObserveGameResultsUseCase
import com.pph.domain.usecases.ObservePlayerNameUseCase
import com.pph.domain.usecases.PlaceQueenUseCase
import com.pph.domain.usecases.SaveGameResultUseCase
import com.pph.game.state.GameScreenState
import com.pph.shared.mappers.toDomain
import com.pph.shared.mappers.toUI
import com.pph.shared.model.CellPosUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val observeBoardNUseCase: ObserveBoardNUseCase,
    private val observePlayerNameUseCase: ObservePlayerNameUseCase,
    private val placeQueenUseCase: PlaceQueenUseCase,
    private val saveGameResultUseCase: SaveGameResultUseCase,
    private val observeGameResultsUseCase: ObserveGameResultsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameScreenState())
    val state: StateFlow<GameScreenState> = _state

    private var tickerJob: Job? = null
    private var resultsJob: Job? = null
    private var startElapsedRealtime: Long = 0L

    init {
        viewModelScope.launch {
            observeBoardNUseCase().distinctUntilChanged().collect { n ->
                setBoardN(n)
            }
        }

        viewModelScope.launch {
            observePlayerNameUseCase().collect { name ->
                updateState(playerName = name)
            }
        }
    }

    fun setBoardN(boardN: Int) {
        resetGame(boardN)
        observeResultsForBoard(boardN)
    }

    fun onBoardTapped() {
        startTimerIfNeeded()
    }

    fun onCellTapped(row: Int, col: Int) {
        startTimerIfNeeded()

        if (_state.value.showWinDialog) return

        val result = placeQueenUseCase(
            boardN = _state.value.boardN,
            currentQueens = _state.value.queens.toDomain(),
            tapped = CellPos(row, col)
        )

        if (result.isSolved) {
            stopTimer()

            val finalTime = _state.value.elapsedMillis

            viewModelScope.launch {
                val results = observeGameResultsUseCase(boardN = _state.value.boardN).first()

                val bestFromDb = results
                    .asSequence()
                    .filter { it.boardN == _state.value.boardN }
                    .minByOrNull { it.elapsedMillis }
                    ?.elapsedMillis

                val newRecord = bestFromDb == null || finalTime < bestFromDb

                updateState(
                    bestTimeMillis = bestFromDb,
                    isNewRecord = newRecord,
                    showWinDialog = true,
                    queens = result.queens.toUI(),
                    conflictLine = result.conflictLine.toUI()
                )

                saveGameResultUseCase(
                    GameResult(
                        boardN = _state.value.boardN,
                        playerName = _state.value.playerName,
                        elapsedMillis = finalTime,
                        finishedAtEpochMillis = System.currentTimeMillis()
                    )
                )
            }
        } else {
            updateState(
                queens = result.queens.toUI(),
                conflictLine = result.conflictLine.toUI(),
                showWinDialog = false,
                isNewRecord = false
            )
        }

    }

    fun onResetClick() {
        resetGame(_state.value.boardN)
    }

    fun hideWinDialog() {
        updateState(showWinDialog = false)
    }

    // ---------------- DataBase ----------------

    private fun observeResultsForBoard(n: Int) {
        resultsJob?.cancel()
        resultsJob = viewModelScope.launch {
            observeGameResultsUseCase(boardN = _state.value.boardN).collect { results ->
                val best = results
                    .asSequence()
                    .filter { it.boardN == n }
                    .minByOrNull { it.elapsedMillis }
                    ?.elapsedMillis

                updateState(bestTimeMillis = best)
            }
        }
    }


    // ---------------- Timer ----------------

    private fun startTimerIfNeeded() {
        if (_state.value.running) return

        startElapsedRealtime = SystemClock.elapsedRealtime()
        updateState(running = true)

        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (true) {
                val now = SystemClock.elapsedRealtime()
                val elapsed = (now - startElapsedRealtime)
                updateState(elapsedMillis = elapsed)
                delay(200L)
            }
        }
    }

    private fun stopTimer() {
        tickerJob?.cancel()
        tickerJob = null
        startElapsedRealtime = 0L
        updateState(running = false)
    }

    // ---------------- State helpers ----------------

    private fun resetGame(boardN: Int) {
        stopTimer()

        updateState(
            boardN = boardN,
            running = false,
            elapsedMillis = 0L,
            queens = emptySet(),
            conflictLine = emptySet(),
            showWinDialog = false,
            isNewRecord = false
        )
    }

    private fun updateState(
        boardN: Int = _state.value.boardN,
        running: Boolean = _state.value.running,
        elapsedMillis: Long = _state.value.elapsedMillis,
        queens: Set<CellPosUi> = _state.value.queens,
        conflictLine: Set<CellPosUi> = _state.value.conflictLine,
        showWinDialog: Boolean = _state.value.showWinDialog,
        bestTimeMillis: Long? = _state.value.bestTimeMillis,
        isNewRecord: Boolean = _state.value.isNewRecord,
        playerName: String = _state.value.playerName
    ) {
        _state.value = GameScreenState(
            boardN = boardN,
            running = running,
            elapsedMillis = elapsedMillis,
            queens = queens,
            conflictLine = conflictLine,
            showWinDialog = showWinDialog,
            bestTimeMillis = bestTimeMillis,
            isNewRecord = isNewRecord,
            playerName = playerName
        )
    }

    override fun onCleared() {
        tickerJob?.cancel()
        resultsJob?.cancel()
        super.onCleared()
    }
}