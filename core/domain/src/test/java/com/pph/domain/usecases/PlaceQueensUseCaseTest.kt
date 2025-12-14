package com.pph.domain.usecases

import com.pph.domain.model.CellPos
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaceQueenUseCaseTest {

    private val useCase = PlaceQueenUseCase()

    @Test
    fun `given empty queens when tap empty cell then adds queen and not solved`() {
        // Given
        val boardN = 4
        val current = emptySet<CellPos>()
        val tapped = CellPos(0, 0)

        // When
        val out = useCase(boardN, current, tapped)

        // Then
        assertEquals(setOf(tapped), out.queens)
        assertTrue(out.conflictLine.isEmpty())
        assertFalse(out.isSolved)
    }

    @Test
    fun `given queen exists when tap same cell then removes queen`() {
        // Given
        val boardN = 4
        val q = CellPos(0, 0)
        val current = setOf(q)

        // When
        val out = useCase(boardN, current, q)

        // Then
        assertTrue(out.queens.isEmpty())
        assertTrue(out.conflictLine.isEmpty())
        assertFalse(out.isSolved)
    }

    @Test
    fun `given less than N queens when tap empty cell then adds queen`() {
        // Given
        val boardN = 4
        val current = setOf(CellPos(0, 0), CellPos(1, 2))
        val tapped = CellPos(3, 3)

        // When
        val out = useCase(boardN, current, tapped)

        // Then
        assertEquals(3, out.queens.size)
        assertTrue(out.queens.contains(tapped))
    }

    @Test
    fun `given already N queens when tap empty cell then does not add another`() {
        // Given
        val boardN = 4
        val current = setOf(
            CellPos(0, 1),
            CellPos(1, 3),
            CellPos(2, 0),
            CellPos(3, 2)
        )
        val tapped = CellPos(0, 0)

        // When
        val out = useCase(boardN, current, tapped)

        // Then
        assertEquals(current, out.queens)
        assertFalse(out.isSolved)
    }

    @Test
    fun `given placing last queen completes valid solution when tap then solved true`() {
        // Given (solución clásica 4-queens)
        val boardN = 4
        val current = setOf(CellPos(0, 1), CellPos(1, 3), CellPos(2, 0))
        val last = CellPos(3, 2)

        // When
        val out = useCase(boardN, current, last)

        // Then
        assertEquals(4, out.queens.size)
        assertTrue(out.conflictLine.isEmpty())
        assertTrue(out.isSolved)
    }

    @Test
    fun `given conflict in same row when computeAllConflictLines then returns full row segment`() {
        // Given
        val a = CellPos(2, 1)
        val b = CellPos(2, 4)

        // When
        val conflicts = useCase.computeAllConflictLines(setOf(a, b))

        // Then
        assertEquals(
            setOf(CellPos(2,1), CellPos(2,2), CellPos(2,3), CellPos(2,4)),
            conflicts
        )
    }

    @Test
    fun `given conflict in same column when computeAllConflictLines then returns full column segment`() {
        // Given
        val a = CellPos(1, 3)
        val b = CellPos(4, 3)

        // When
        val conflicts = useCase.computeAllConflictLines(setOf(a, b))

        // Then
        assertEquals(
            setOf(CellPos(1,3), CellPos(2,3), CellPos(3,3), CellPos(4,3)),
            conflicts
        )
    }

    @Test
    fun `given conflict in main diagonal when computeAllConflictLines then returns diagonal segment`() {
        // Given
        val a = CellPos(0, 0)
        val b = CellPos(3, 3)

        // When
        val conflicts = useCase.computeAllConflictLines(setOf(a, b))

        // Then
        assertEquals(setOf(CellPos(0,0), CellPos(1,1), CellPos(2,2), CellPos(3,3)), conflicts)
    }

    @Test
    fun `given conflict in anti diagonal when computeAllConflictLines then returns diagonal segment`() {
        // Given
        val a = CellPos(0, 3)
        val b = CellPos(3, 0)

        // When
        val conflicts = useCase.computeAllConflictLines(setOf(a, b))

        // Then
        assertEquals(setOf(CellPos(0,3), CellPos(1,2), CellPos(2,1), CellPos(3,0)), conflicts)
    }

    @Test
    fun `given no conflicts when computeAllConflictLines then empty`() {
        // Given
        val queens = setOf(CellPos(0, 1), CellPos(1, 3))

        // When
        val conflicts = useCase.computeAllConflictLines(queens)

        // Then
        assertTrue(conflicts.isEmpty())
    }
}
