package com.pph.shared.mappers

import com.pph.domain.model.CellPos
import com.pph.shared.model.CellPosUi
import org.junit.Assert.assertEquals
import org.junit.Test

class CellPosMapperTest {

    @Test
    fun `given domain cell when mapping to UI then row and col are preserved`() {
        // Given
        val domain = CellPos(row = 1, col = 2)

        // When
        val ui = domain.toUI()

        // Then
        assertEquals(CellPosUi(row = 1, col = 2), ui)
    }

    @Test
    fun `given UI cell when mapping to domain then row and col are preserved`() {
        // Given
        val ui = CellPosUi(row = 3, col = 4)

        // When
        val domain = ui.toDomain()

        // Then
        assertEquals(CellPos(row = 3, col = 4), domain)
    }

    @Test
    fun `given set of domain cells when mapping to UI then size and values match`() {
        // Given
        val set = setOf(CellPos(0, 0), CellPos(1, 2))

        // When
        val uiSet = set.toUI()

        // Then
        assertEquals(setOf(CellPosUi(0, 0), CellPosUi(1, 2)), uiSet)
    }

    @Test
    fun `given set of UI cells when mapping to domain then size and values match`() {
        // Given
        val set = setOf(CellPosUi(2, 2), CellPosUi(3, 1))

        // When
        val domainSet = set.toDomain()

        // Then
        assertEquals(setOf(CellPos(2, 2), CellPos(3, 1)), domainSet)
    }
}
