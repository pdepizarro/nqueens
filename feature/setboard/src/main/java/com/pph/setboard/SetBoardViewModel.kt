package com.pph.setboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pph.domain.usecases.ObserveGameResultsUseCase
import com.pph.domain.usecases.ObserveBoardNUseCase
import com.pph.domain.usecases.ObservePlayerNameUseCase
import com.pph.domain.usecases.SaveGamePrefsUseCase
import com.pph.setboard.event.SetBoardScreenEvent
import com.pph.setboard.state.SetBoardScreenState
import com.pph.shared.mappers.toUi
import com.pph.shared.model.GameResultUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SetBoardViewModel @Inject constructor(
    private val observeGameResultsUseCase: ObserveGameResultsUseCase,
    private val observeBoardNUseCase: ObserveBoardNUseCase,
    private val observePlayerNameUseCase: ObservePlayerNameUseCase,
    private val saveGamePrefsUseCase: SaveGamePrefsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SetBoardScreenState())
    val state: StateFlow<SetBoardScreenState> = _state

    init {
        viewModelScope.launch {
            observeBoardNUseCase().collect { n ->
                updateState(boardN = n.toString())
            }
        }
        viewModelScope.launch {
            observePlayerNameUseCase().collect { name ->
                updateState(playerName = name)
            }
        }

        viewModelScope.launch {
            _state
                .map { it.boardNInt }
                .distinctUntilChanged()
                .flatMapLatest { n ->
                    if (n in 4..9) {
                        observeGameResultsUseCase(n)
                    } else flowOf(emptyList())
                }
                .collect { results ->
                    updateState(scores = results.toUi())
                }
        }
    }

    private val _navigateNextEvent = Channel<SetBoardScreenEvent>(Channel.BUFFERED)
    val navigateNextEvent = _navigateNextEvent.receiveAsFlow()


    fun onPlayerNameChanged(value: String) {
        updateState(playerName = value)
    }

    fun onBoardNChanged(value: String) {
        updateState(boardN = value)
    }

    fun onRandomPositionClick() {
        val randomN = (4..9).random().toString()

        updateState(boardN = randomN)
    }

    fun onSubmitClick() {
        val state = _state.value
        updateState(showErrors = true)
        if (!state.canStart) return


        viewModelScope.launch {
            saveGamePrefsUseCase(state.boardNInt, state.playerName.trim())
            _navigateNextEvent.send(SetBoardScreenEvent.NavigateNext)
        }
    }

    private fun updateState(
        boardN: String = _state.value.boardN,
        playerName: String = _state.value.playerName,
        showErrors: Boolean = _state.value.showErrors,
        scores: List<GameResultUi> = _state.value.scores
    ) {
        _state.value = _state.value.copy(
            boardN = boardN,
            playerName = playerName,
            showErrors = showErrors,
            scores = scores
        )
    }
}