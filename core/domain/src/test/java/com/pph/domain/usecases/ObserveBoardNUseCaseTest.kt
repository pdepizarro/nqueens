package com.pph.domain.usecases

import com.pph.domain.repository.GamePrefsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertSame

class ObserveBoardNUseCaseTest {

    @Test
    fun `given repository flow when invoked then returns same flow and calls repo once`() {
        // Given
        val repo = mockk<GamePrefsRepository>()
        val expected = flowOf(6)
        every { repo.observeBoardN() } returns expected
        val useCase = ObserveBoardNUseCase(repo)

        // When
        val result = useCase()

        // Then
        assertSame(expected, result)
        verify(exactly = 1) { repo.observeBoardN() }
    }
}
