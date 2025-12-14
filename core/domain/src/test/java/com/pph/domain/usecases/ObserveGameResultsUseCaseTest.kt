package com.pph.domain.usecases

import com.pph.domain.model.GameResult
import com.pph.domain.repository.GameResultRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertSame

class ObserveGameResultsUseCaseTest {

    @Test
    fun `given boardN when invoked then delegates to repo observeLatest with same boardN`() {
        // Given
        val repo = mockk<GameResultRepository>()
        val expected = flowOf(emptyList<GameResult>())
        every { repo.observeLatest(5) } returns expected
        val useCase = ObserveGameResultsUseCase(repo)

        // When
        val result = useCase(5)

        // Then
        assertSame(expected, result)
        verify(exactly = 1) { repo.observeLatest(5) }
    }
}
