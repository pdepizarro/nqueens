package com.pph.setboard.state

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SetBoardScreenStateTest {

    @Test
    fun `given blank player name when validating then isPlayerNameValid is false`() {
        // Given
        val state = SetBoardScreenState(playerName = "   ")

        // When
        val valid = state.isPlayerNameValid

        // Then
        assertFalse(valid)
    }

    @Test
    fun `given invalid N when validating then isNValid is false`() {
        // Given
        val state = SetBoardScreenState(boardN = "abc")

        // When
        val valid = state.isNValid

        // Then
        assertFalse(valid)
    }

    @Test
    fun `given N out of range when validating then isNValid is false`() {
        // Given
        val low = SetBoardScreenState(boardN = "3")
        val high = SetBoardScreenState(boardN = "10")

        // Then
        assertFalse(low.isNValid)
        assertFalse(high.isNValid)
    }

    @Test
    fun `given valid N and player name when checking canStart then canStart is true`() {
        // Given
        val state = SetBoardScreenState(boardN = "6", playerName = "Ana")

        // When
        val canStart = state.canStart

        // Then
        assertTrue(canStart)
    }

    @Test
    fun `given invalid N or invalid name when checking canStart then canStart is false`() {
        // Given
        val invalidN = SetBoardScreenState(boardN = "2", playerName = "Ana")
        val invalidName = SetBoardScreenState(boardN = "6", playerName = "")

        // Then
        assertFalse(invalidN.canStart)
        assertFalse(invalidName.canStart)
    }
}
