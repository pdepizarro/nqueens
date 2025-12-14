package com.pph.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val boardN: Int,
    val playerName: String,
    val elapsedMillis: Long,
    val finishedAtEpochMillis: Long
)