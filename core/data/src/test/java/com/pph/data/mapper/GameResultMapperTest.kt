package com.pph.data.mapper

import com.pph.data.model.entity.GameResultEntity
import com.pph.domain.model.GameResult
import org.junit.Assert.assertEquals
import org.junit.Test

class GameResultMapperTest {

    @Test
    fun `given domain model when mapping to entity then all fields are copied`() {
        // Given
        val domain = GameResult(
            id = 5,
            playerName = "Morrison",
            boardN = 8,
            elapsedMillis = 123_456,
            finishedAtEpochMillis = 999
        )

        // When
        val entity = domain.toEntity()

        // Then
        assertEquals(
            GameResultEntity(
                id = 5,
                playerName = "Morrison",
                boardN = 8,
                elapsedMillis = 123_456,
                finishedAtEpochMillis = 999
            ),
            entity
        )
    }

    @Test
    fun `given entity when mapping to domain then all fields are copied`() {
        // Given
        val entity = GameResultEntity(
            id = 7,
            playerName = "Ana",
            boardN = 4,
            elapsedMillis = 42,
            finishedAtEpochMillis = 123
        )

        // When
        val domain = entity.toDomain()

        // Then
        assertEquals(
            GameResult(
                id = 7,
                playerName = "Ana",
                boardN = 4,
                elapsedMillis = 42,
                finishedAtEpochMillis = 123
            ),
            domain
        )
    }
}
