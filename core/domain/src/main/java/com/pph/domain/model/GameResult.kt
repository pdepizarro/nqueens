package com.pph.domain.model

data class GameResult(
    val id: Long = 0L,
    val playerName: String,
    val boardN: Int,
    val elapsedMillis: Long,
    val finishedAtEpochMillis: Long
)