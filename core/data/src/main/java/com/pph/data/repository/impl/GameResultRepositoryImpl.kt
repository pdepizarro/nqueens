package com.pph.data.repository.impl

import com.pph.data.mapper.toDomain
import com.pph.data.mapper.toEntity
import com.pph.data.model.dao.GameResultDao
import com.pph.domain.model.GameResult
import com.pph.domain.repository.GameResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameResultRepositoryImpl @Inject constructor(
    private val dao: GameResultDao
) : GameResultRepository {

    override suspend fun insert(result: GameResult) {
        dao.insert(result.toEntity())
    }

    override fun observeLatest(boardN: Int): Flow<List<GameResult>> {
        return dao.observeLatest(boardN).map { list -> list.map { it.toDomain() } }
    }
}
