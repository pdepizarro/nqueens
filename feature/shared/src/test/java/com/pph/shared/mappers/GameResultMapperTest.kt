package com.pph.shared.mappers

import com.pph.domain.model.GameResult
import com.pph.shared.model.GameResultUi
import org.junit.Assert.assertEquals
import org.junit.Test

class GameResultMapperTest {

    @Test
    fun `given domain result when mapping to ui then all fields are copied`() {
        // Given
        val domain = GameResult(
            id = 9,
            playerName = "P",
            boardN = 5,
            elapsedMillis = 777,
            finishedAtEpochMillis = 999
        )

        // When
        val ui = domain.toUi()

        // Then
        assertEquals(
            GameResultUi(
                id = 9,
                playerName = "P",
                boardN = 5,
                elapsedMillis = 777,
                finishedAtEpochMillis = 999
            ),
            ui
        )
    }

    @Test
    fun `given list of domain results when mapping to ui then preserves order and size`() {
        // Given
        val list = listOf(
            GameResult(1, "A", 4, 100, 1000),
            GameResult(2, "B", 4, 200, 2000)
        )

        // When
        val ui = list.toUi()

        // Then
        assertEquals(
            listOf(
                GameResultUi(1, "A", 4, 100, 1000),
                GameResultUi(2, "B", 4, 200, 2000)
            ),
            ui
        )
    }
}
