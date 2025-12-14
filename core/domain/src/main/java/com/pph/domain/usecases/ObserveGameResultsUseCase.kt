package com.pph.domain.usecases

import com.pph.domain.model.GameResult
import com.pph.domain.repository.GameResultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGameResultsUseCase @Inject constructor(
    private val repo: GameResultRepository
) {
    operator fun invoke(boardN: Int): Flow<List<GameResult>> = repo.observeLatest(boardN)
}