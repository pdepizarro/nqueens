package com.pph.domain.usecases

import com.pph.domain.model.GameResult
import com.pph.domain.repository.GameResultRepository
import javax.inject.Inject

class SaveGameResultUseCase @Inject constructor(
    private val repo: GameResultRepository
) {
    suspend operator fun invoke(result: GameResult) = repo.insert(result)
}