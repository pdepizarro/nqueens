package com.pph.data.repository.impl

import com.pph.data.local.prefs.GamePrefsDataStore
import com.pph.domain.repository.GamePrefsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GamePrefsRepositoryImpl @Inject constructor(
    private val ds: GamePrefsDataStore
) : GamePrefsRepository {
    override fun observeBoardN(): Flow<Int> = ds.boardNFlow
    override fun observePlayerName(): Flow<String> = ds.playerNameFlow
    override suspend fun setGameArgs(boardN: Int, playerName: String) = ds.setGameArgs(boardN, playerName)
}
