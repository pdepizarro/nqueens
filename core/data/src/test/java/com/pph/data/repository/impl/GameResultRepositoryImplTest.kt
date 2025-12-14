package com.pph.data.repository.impl

import com.pph.data.model.dao.GameResultDao
import com.pph.data.model.entity.GameResultEntity
import com.pph.domain.model.GameResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GameResultRepositoryImplTest {

    @Test
    fun `given result when insert then dao receives entity`() = runTest {
        // Given
        val dao = mockk<GameResultDao>()
        coEvery { dao.insert(any()) } returns Unit
        val repo = GameResultRepositoryImpl(dao)

        val domain = GameResult(
            id = 1,
            playerName = "P",
            boardN = 5,
            elapsedMillis = 10,
            finishedAtEpochMillis = 20
        )

        // When
        repo.insert(domain)

        // Then
        coVerify(exactly = 1) {
            dao.insert(
                GameResultEntity(
                    id = 1,
                    playerName = "P",
                    boardN = 5,
                    elapsedMillis = 10,
                    finishedAtEpochMillis = 20
                )
            )
        }
    }

    @Test
    fun `given dao flow when observeLatest then maps entities to domain`() = runTest {
        // Given
        val dao = mockk<GameResultDao>()
        val entities = listOf(
            GameResultEntity(1, 4, "A", 100, 1000),
            GameResultEntity(2, 4, "B", 200, 2000)
        )
        every { dao.observeLatest(4) } returns flowOf(entities)
        val repo = GameResultRepositoryImpl(dao)

        // When
        val emitted = repo.observeLatest(4).single()

        // Then
        assertEquals(
            listOf(
                GameResult(1, "A", 4, 100, 1000),
                GameResult(2, "B", 4, 200, 2000)
            ),
            emitted
        )
    }
}
