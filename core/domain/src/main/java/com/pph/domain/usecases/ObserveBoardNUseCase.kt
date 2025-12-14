package com.pph.domain.usecases

import com.pph.domain.repository.GamePrefsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBoardNUseCase @Inject constructor(
    private val repo: GamePrefsRepository
) { operator fun invoke(): Flow<Int> = repo.observeBoardN() }