package com.pph.domain.repository

import com.pph.domain.model.GameResult
import kotlinx.coroutines.flow.Flow

interface GameResultRepository {
    suspend fun insert(result: GameResult)
    fun observeLatest(boardN: Int): Flow<List<GameResult>>
}