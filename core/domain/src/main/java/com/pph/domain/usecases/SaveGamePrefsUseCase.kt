package com.pph.domain.usecases

import com.pph.domain.repository.GamePrefsRepository
import javax.inject.Inject

class SaveGamePrefsUseCase @Inject constructor(
    private val repo: GamePrefsRepository
) { suspend operator fun invoke(boardN: Int, playerName: String) = repo.setGameArgs(boardN, playerName) }