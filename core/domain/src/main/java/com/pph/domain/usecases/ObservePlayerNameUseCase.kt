package com.pph.domain.usecases

import com.pph.domain.repository.GamePrefsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePlayerNameUseCase @Inject constructor(
    private val repo: GamePrefsRepository
) { operator fun invoke(): Flow<String> = repo.observePlayerName() }