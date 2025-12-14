package com.pph.shared.model

data class GameResultUi(
    val id: Long = 0L,
    val playerName: String,
    val boardN: Int,
    val elapsedMillis: Long,
    val finishedAtEpochMillis: Long
)