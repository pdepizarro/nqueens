package com.pph.data.repository.impl

import com.pph.data.local.prefs.GamePrefsDataStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GamePrefsRepositoryImplTest {

    @Test
    fun `given repo when observeBoardN then delegates to datastore flow`() {
        // Given
        val ds = mockk<GamePrefsDataStore>()
        val flow = flowOf(8)
        every { ds.boardNFlow } returns flow
        val repo = GamePrefsRepositoryImpl(ds)

        // When
        val out = repo.observeBoardN()

        // Then
        Assert.assertSame(flow, out)
    }

    @Test
    fun `given repo when observePlayerName then delegates to datastore flow`() {
        // Given
        val ds = mockk<GamePrefsDataStore>()
        val flow = flowOf("pepe")
        every { ds.playerNameFlow } returns flow
        val repo = GamePrefsRepositoryImpl(ds)

        // When
        val out = repo.observePlayerName()

        // Then
        Assert.assertSame(flow, out)
    }

    @Test
    fun `given boardN and playerName when setGameArgs then delegates to datastore`() = runTest {
        // Given
        val ds = mockk<GamePrefsDataStore>()
        coEvery { ds.setGameArgs(any(), any()) } returns Unit
        val repo = GamePrefsRepositoryImpl(ds)

        // When
        repo.setGameArgs(6, " Ana ")

        // Then
        coVerify(exactly = 1) { ds.setGameArgs(6, " Ana ") }
    }
}