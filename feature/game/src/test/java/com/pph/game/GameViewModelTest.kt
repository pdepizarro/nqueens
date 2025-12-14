package com.pph.game

import com.pph.domain.model.CellPos
import com.pph.domain.model.GameResult
import com.pph.domain.model.NQueensResult
import com.pph.domain.usecases.ObserveBoardNUseCase
import com.pph.domain.usecases.ObserveGameResultsUseCase
import com.pph.domain.usecases.ObservePlayerNameUseCase
import com.pph.domain.usecases.PlaceQueenUseCase
import com.pph.domain.usecases.SaveGameResultUseCase
import com.pph.shared.mappers.toUI
import com.pph.testutil.MainDispatcherRule
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    @Test
    fun `given initial flows when viewModel starts then boardN and playerName are updated`() =
        runTest {
            // Given
            val observeBoardN = mockk<ObserveBoardNUseCase>()
            val observePlayerName = mockk<ObservePlayerNameUseCase>()
            val placeQueen = mockk<PlaceQueenUseCase>()
            val saveResult = mockk<SaveGameResultUseCase>()
            val observeResults = mockk<ObserveGameResultsUseCase>()

            every { observeBoardN.invoke() } returns flowOf(6)
            every { observePlayerName.invoke() } returns flowOf("Ana")
            every { observeResults.invoke(any()) } returns flowOf(emptyList())
            every { placeQueen.invoke(any(), any(), any()) } returns
                    NQueensResult(emptySet(), emptySet(), false)
            coEvery { saveResult.invoke(any()) } returns Unit

            // When
            val vm = GameViewModel(
                observeBoardN,
                observePlayerName,
                placeQueen,
                saveResult,
                observeResults
            )
            advanceUntilIdle()

            // Then
            assertEquals(6, vm.state.value.boardN)
            assertEquals("Ana", vm.state.value.playerName)
        }

    @Test
    fun `given results flow when observing then bestTimeMillis is min elapsed for that board`() =
        runTest {
            // Given
            val observeBoardN = mockk<ObserveBoardNUseCase>()
            val observePlayerName = mockk<ObservePlayerNameUseCase>()
            val placeQueen = mockk<PlaceQueenUseCase>()
            val saveResult = mockk<SaveGameResultUseCase>()
            val observeResults = mockk<ObserveGameResultsUseCase>()

            every { observeBoardN.invoke() } returns flowOf(5)
            every { observePlayerName.invoke() } returns flowOf("")
            every { placeQueen.invoke(any(), any(), any()) } returns
                    NQueensResult(emptySet(), emptySet(), false)
            coEvery { saveResult.invoke(any()) } returns Unit

            val db = listOf(
                GameResult(1, "A", 5, 300, 1),
                GameResult(2, "B", 5, 120, 2),
                GameResult(3, "C", 4, 10, 3) // distinto boardN → ignorado
            )
            every { observeResults.invoke(5) } returns flowOf(db)

            // When
            val vm = GameViewModel(
                observeBoardN,
                observePlayerName,
                placeQueen,
                saveResult,
                observeResults
            )
            advanceUntilIdle()

            // Then
            assertEquals(120L, vm.state.value.bestTimeMillis)
        }

    @Test
    fun `given showWinDialog true when cell tapped then placeQueen is not called and queens stay unchanged`() =
        runTest {
            // Given
            val observeBoardN = mockk<ObserveBoardNUseCase>()
            val observePlayerName = mockk<ObservePlayerNameUseCase>()
            val placeQueen = mockk<PlaceQueenUseCase>()
            val saveResult = mockk<SaveGameResultUseCase>()
            val observeResults = mockk<ObserveGameResultsUseCase>()

            every { observeBoardN.invoke() } returns flowOf(4)
            every { observePlayerName.invoke() } returns flowOf("P")
            every { observeResults.invoke(any()) } returns flowOf(emptyList())
            coEvery { saveResult.invoke(any()) } returns Unit

            val solved = NQueensResult(
                queens = setOf(
                    CellPos(0, 1),
                    CellPos(1, 3),
                    CellPos(2, 0),
                    CellPos(3, 2)
                ),
                conflictLine = emptySet(),
                isSolved = true
            )
            every { placeQueen.invoke(any(), any(), any()) } returns solved

            val vm = spiedViewModel(
                observeBoardN,
                observePlayerName,
                placeQueen,
                saveResult,
                observeResults
            )
            advanceUntilIdle()

            // First tap → win
            vm.onCellTapped(0, 0)
            advanceUntilIdle()
            val queensAfterWin = vm.state.value.queens

            clearMocks(placeQueen, answers = false)

            // Second tap → ignored
            vm.onCellTapped(1, 1)
            advanceUntilIdle()

            verify(exactly = 0) { placeQueen.invoke(any(), any(), any()) }
            assertEquals(queensAfterWin, vm.state.value.queens)
        }

    @Test
    fun `given non solved placement when cell tapped then updates queens and does not save result`() =
        runTest {
            // Given
            val observeBoardN = mockk<ObserveBoardNUseCase>()
            val observePlayerName = mockk<ObservePlayerNameUseCase>()
            val placeQueen = mockk<PlaceQueenUseCase>()
            val saveResult = mockk<SaveGameResultUseCase>()
            val observeResults = mockk<ObserveGameResultsUseCase>()

            every { observeBoardN.invoke() } returns flowOf(4)
            every { observePlayerName.invoke() } returns flowOf("P")
            every { observeResults.invoke(any()) } returns flowOf(emptyList())
            coEvery { saveResult.invoke(any()) } returns Unit

            val nqr = NQueensResult(
                queens = setOf(CellPos(0, 0)),
                conflictLine = setOf(CellPos(0, 0), CellPos(0, 1)),
                isSolved = false
            )
            every { placeQueen.invoke(any(), any(), any()) } returns nqr

            val vm = spiedViewModel(
                observeBoardN,
                observePlayerName,
                placeQueen,
                saveResult,
                observeResults
            )
            advanceUntilIdle()

            // When
            vm.onCellTapped(0, 0)
            advanceUntilIdle()

            // Then
            assertEquals(nqr.queens.toUI(), vm.state.value.queens)
            assertEquals(nqr.conflictLine.toUI(), vm.state.value.conflictLine)
            assertFalse(vm.state.value.showWinDialog)
            assertFalse(vm.state.value.isNewRecord)
            coVerify(exactly = 0) { saveResult.invoke(any()) }
        }

    @Test
    fun `given solved placement when cell tapped then shows win and saves result`() = runTest {
        // Given
        val observeBoardN = mockk<ObserveBoardNUseCase>()
        val observePlayerName = mockk<ObservePlayerNameUseCase>()
        val placeQueen = mockk<PlaceQueenUseCase>()
        val saveResult = mockk<SaveGameResultUseCase>()
        val observeResults = mockk<ObserveGameResultsUseCase>()

        every { observeBoardN.invoke() } returns flowOf(4)
        every { observePlayerName.invoke() } returns flowOf("Player")
        every { observeResults.invoke(4) } returns flowOf(emptyList())
        coEvery { saveResult.invoke(any()) } returns Unit

        val solved = NQueensResult(
            queens = setOf(
                CellPos(0, 1),
                CellPos(1, 3),
                CellPos(2, 0),
                CellPos(3, 2)
            ),
            conflictLine = emptySet(),
            isSolved = true
        )
        every { placeQueen.invoke(any(), any(), any()) } returns solved

        val vm = spiedViewModel(
            observeBoardN,
            observePlayerName,
            placeQueen,
            saveResult,
            observeResults
        )
        advanceUntilIdle()

        // When
        vm.onCellTapped(0, 0)
        advanceUntilIdle()

        // Then
        assertTrue(vm.state.value.showWinDialog)
        assertEquals(solved.queens.toUI(), vm.state.value.queens)
        coVerify(exactly = 1) { saveResult.invoke(any()) }
        assertFalse(vm.state.value.running)
    }
}

private fun spiedViewModel(
    observeBoardN: ObserveBoardNUseCase,
    observePlayerName: ObservePlayerNameUseCase,
    placeQueen: PlaceQueenUseCase,
    saveResult: SaveGameResultUseCase,
    observeResults: ObserveGameResultsUseCase
): GameViewModel {
    val vm = spyk(
        GameViewModel(
            observeBoardN,
            observePlayerName,
            placeQueen,
            saveResult,
            observeResults
        ),
        recordPrivateCalls = true
    )

    coJustRun { vm["startTimerIfNeeded"]() }

    return vm
}