package com.pph.setboard

import com.pph.domain.model.GameResult
import com.pph.domain.usecases.ObserveBoardNUseCase
import com.pph.domain.usecases.ObserveGameResultsUseCase
import com.pph.domain.usecases.ObservePlayerNameUseCase
import com.pph.domain.usecases.SaveGamePrefsUseCase
import com.pph.setboard.event.SetBoardScreenEvent
import com.pph.testutil.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetBoardViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    @Test
    fun `given repo flows when viewModel starts then state is seeded from boardN and playerName`() =
        runTest {
            // Given
            val boardNFlow = MutableSharedFlow<Int>(replay = 1)
            val nameFlow = MutableSharedFlow<String>(replay = 1)

            val observeBoardN = mockk<ObserveBoardNUseCase>()
            val observePlayerName = mockk<ObservePlayerNameUseCase>()
            val observeResults = mockk<ObserveGameResultsUseCase>()
            val savePrefs = mockk<SaveGamePrefsUseCase>()

            every { observeBoardN.invoke() } returns boardNFlow
            every { observePlayerName.invoke() } returns nameFlow
            every { observeResults.invoke(any()) } returns flowOf(emptyList())
            coEvery { savePrefs.invoke(any(), any()) } returns Unit

            boardNFlow.emit(7)
            nameFlow.emit("  Ana  ")

            // When
            val vm = SetBoardViewModel(observeResults, observeBoardN, observePlayerName, savePrefs)
            advanceUntilIdle()

            // Then
            assertEquals("7", vm.state.value.boardN)
            assertEquals("  Ana  ", vm.state.value.playerName)
        }

    @Test
    fun `given valid boardN when boardN changes then viewModel observes results and maps to UI`() =
        runTest {
            // Given
            val boardNFlow = kotlinx.coroutines.flow.MutableStateFlow(5)
            val nameFlow = kotlinx.coroutines.flow.MutableStateFlow("X")

            val observeBoardN = mockk<ObserveBoardNUseCase>()
            val observePlayerName = mockk<ObservePlayerNameUseCase>()
            val observeResults = mockk<ObserveGameResultsUseCase>()
            val savePrefs = mockk<SaveGamePrefsUseCase>()

            every { observeBoardN.invoke() } returns boardNFlow
            every { observePlayerName.invoke() } returns nameFlow
            coEvery { savePrefs.invoke(any(), any()) } returns Unit

            val dbResults = listOf(
                GameResult(1, "A", 5, 100, 1),
                GameResult(2, "B", 5, 200, 2)
            )

            every { observeResults.invoke(any()) } answers {
                val n = firstArg<Int>()
                if (n == 5) flowOf(dbResults) else flowOf(emptyList())
            }

            // When
            val vm = SetBoardViewModel(observeResults, observeBoardN, observePlayerName, savePrefs)
            advanceUntilIdle()

            // Then
            assertEquals(2, vm.state.value.scores.size)
            assertEquals("A", vm.state.value.scores[0].playerName)
            assertEquals(100, vm.state.value.scores[0].elapsedMillis)
        }


    @Test
    fun `given invalid boardN when boardNInt not in range then scores are emptied`() = runTest {
        // Given
        val boardNFlow = MutableSharedFlow<Int>(replay = 1)
        val nameFlow = MutableSharedFlow<String>(replay = 1)

        val observeBoardN = mockk<ObserveBoardNUseCase>()
        val observePlayerName = mockk<ObservePlayerNameUseCase>()
        val observeResults = mockk<ObserveGameResultsUseCase>()
        val savePrefs = mockk<SaveGamePrefsUseCase>()

        every { observeBoardN.invoke() } returns boardNFlow
        every { observePlayerName.invoke() } returns nameFlow
        every { observeResults.invoke(any()) } returns flowOf(listOf(GameResult(1, "A", 10, 1, 1)))
        coEvery { savePrefs.invoke(any(), any()) } returns Unit

        boardNFlow.emit(10) // out of range
        nameFlow.emit("A")

        // When
        val vm = SetBoardViewModel(observeResults, observeBoardN, observePlayerName, savePrefs)
        advanceUntilIdle()

        // Then
        assertTrue(vm.state.value.scores.isEmpty())
    }

    @Test
    fun `given invalid form when submit then shows errors and does not navigate or save`() =
        runTest {
            // Given
            val observeBoardN = mockk<ObserveBoardNUseCase>()
            val observePlayerName = mockk<ObservePlayerNameUseCase>()
            val observeResults = mockk<ObserveGameResultsUseCase>()
            val savePrefs = mockk<SaveGamePrefsUseCase>()

            every { observeBoardN.invoke() } returns flowOf(5)
            every { observePlayerName.invoke() } returns flowOf("") // invalid
            every { observeResults.invoke(any()) } returns flowOf(emptyList())
            coEvery { savePrefs.invoke(any(), any()) } returns Unit

            val vm = SetBoardViewModel(observeResults, observeBoardN, observePlayerName, savePrefs)
            advanceUntilIdle()

            // When
            vm.onSubmitClick()
            advanceUntilIdle()

            // Then
            assertTrue(vm.state.value.showErrors)
            coVerify(exactly = 0) { savePrefs.invoke(any(), any()) }
        }

    @Test
    fun `given valid form when submit then saves prefs and emits navigate event`() = runTest {
        // Given
        val observeBoardN = mockk<ObserveBoardNUseCase>()
        val observePlayerName = mockk<ObservePlayerNameUseCase>()
        val observeResults = mockk<ObserveGameResultsUseCase>()
        val savePrefs = mockk<SaveGamePrefsUseCase>()

        every { observeBoardN.invoke() } returns flowOf(6)
        every { observePlayerName.invoke() } returns flowOf("  Ana  ")
        every { observeResults.invoke(any()) } returns flowOf(emptyList())
        coEvery { savePrefs.invoke(any(), any()) } returns Unit

        val vm = SetBoardViewModel(observeResults, observeBoardN, observePlayerName, savePrefs)
        advanceUntilIdle()

        // When
        vm.onSubmitClick()
        val event = vm.navigateNextEvent.first()
        advanceUntilIdle()

        // Then
        assertEquals(SetBoardScreenEvent.NavigateNext, event)
        coVerify(exactly = 1) { savePrefs.invoke(6, "Ana") }
        assertTrue(vm.state.value.showErrors)
        assertTrue(vm.state.value.canStart)
    }

    @Test
    fun `when random position clicked then boardN is set within valid range`() = runTest {
        // Given
        val observeBoardN = mockk<ObserveBoardNUseCase>()
        val observePlayerName = mockk<ObservePlayerNameUseCase>()
        val observeResults = mockk<ObserveGameResultsUseCase>()
        val savePrefs = mockk<SaveGamePrefsUseCase>()

        every { observeBoardN.invoke() } returns flowOf(5)
        every { observePlayerName.invoke() } returns flowOf("X")
        every { observeResults.invoke(any()) } returns flowOf(emptyList())
        coEvery { savePrefs.invoke(any(), any()) } returns Unit

        val vm = SetBoardViewModel(observeResults, observeBoardN, observePlayerName, savePrefs)
        advanceUntilIdle()

        // When
        vm.onRandomPositionClick()

        // Then
        val n = vm.state.value.boardN.toInt()
        assertTrue(n in 4..9)
    }
}
