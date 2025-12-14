package com.pph.domain.usecases

import com.pph.domain.model.CellPos
import com.pph.domain.model.NQueensResult
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.sign

class PlaceQueenUseCase @Inject constructor() {
    operator fun invoke(
        boardN: Int,
        currentQueens: Set<CellPos>,
        tapped: CellPos
    ): NQueensResult {

        if (tapped in currentQueens) {
            val newQueens = currentQueens - tapped
            val conflicts = computeAllConflictLines(newQueens)

            return NQueensResult(
                queens = newQueens,
                conflictLine = conflicts,
                isSolved = false
            )
        }

        if (currentQueens.size >= boardN) {
            return NQueensResult(
                queens = currentQueens,
                conflictLine = computeAllConflictLines(currentQueens),
                isSolved = false
            )
        }

        val newQueens = currentQueens + tapped
        val conflicts = computeAllConflictLines(newQueens)

        val solved = newQueens.size == boardN && conflicts.isEmpty()

        return NQueensResult(
            queens = newQueens,
            conflictLine = conflicts,
            isSolved = solved
        )
    }

    // ---------- pure logic ----------

    private fun threatens(a: CellPos, b: CellPos): Boolean =
        a.row == b.row ||
                a.col == b.col ||
                abs(a.row - b.row) == abs(a.col - b.col)

    private fun lineBetweenInclusive(a: CellPos, b: CellPos): List<CellPos> {
        val dr = (b.row - a.row).sign
        val dc = (b.col - a.col).sign

        val out = mutableListOf<CellPos>()
        var r = a.row
        var c = a.col
        out += CellPos(r, c)

        while (r != b.row || c != b.col) {
            r += dr
            c += dc
            out += CellPos(r, c)
        }
        return out
    }

    fun computeAllConflictLines(queens: Set<CellPos>): Set<CellPos> {
        val out = mutableSetOf<CellPos>()

        val list = queens.toList()
        for (i in list.indices) {
            for (j in i + 1 until list.size) {
                val a = list[i]
                val b = list[j]
                if (threatens(a, b)) {
                    out += lineBetweenInclusive(a, b)
                }
            }
        }
        return out
    }
}