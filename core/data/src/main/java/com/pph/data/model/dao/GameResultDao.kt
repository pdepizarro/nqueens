package com.pph.data.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pph.data.model.entity.GameResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GameResultEntity)

    @Query("SELECT * FROM game_results WHERE boardN = :boardN ORDER BY elapsedMillis ASC LIMIT 50")
    fun observeLatest(boardN: Int): Flow<List<GameResultEntity>>

    @Query("DELETE FROM game_results")
    suspend fun clearAll()
}
