package com.pph.domain.usecases

import com.pph.domain.model.GameResult
import com.pph.domain.repository.GameResultRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SaveGameResultUseCaseTest {

    @Test
    fun `given game result when invoked then calls repo insert`() = runTest {
        // Given
        val repo = mockk<GameResultRepository>()
        coEvery { repo.insert(any()) } returns Unit
        val useCase = SaveGameResultUseCase(repo)
        val result = GameResult(1, "P", 4, 100, 999)

        // When
        useCase(result)

        // Then
        coVerify(exactly = 1) { repo.insert(result) }
    }
}
