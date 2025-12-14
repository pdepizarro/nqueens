package com.pph.domain.usecases

import com.pph.domain.repository.GamePrefsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SaveGamePrefsUseCaseTest {

    @Test
    fun `given boardN and name when invoked then calls repo setGameArgs with same args`() =
        runTest {
            // Given
            val repo = mockk<GamePrefsRepository>()
            coEvery { repo.setGameArgs(any(), any()) } returns Unit
            val useCase = SaveGamePrefsUseCase(repo)

            // When
            useCase(7, " Ana ")

            // Then
            coVerify(exactly = 1) { repo.setGameArgs(7, " Ana ") }
        }
}
