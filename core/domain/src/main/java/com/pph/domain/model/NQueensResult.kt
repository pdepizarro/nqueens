package com.pph.domain.model

data class NQueensResult (
    val queens: Set<CellPos>,
    val conflictLine: Set<CellPos>,
    val isSolved: Boolean
)