package com.pph.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pph.data.model.dao.GameResultDao
import com.pph.data.model.entity.GameResultEntity

@Database(
    entities = [GameResultEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao
}
