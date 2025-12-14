package com.pph.domain.repository

import kotlinx.coroutines.flow.Flow

interface GamePrefsRepository {
    fun observeBoardN(): Flow<Int>
    fun observePlayerName(): Flow<String>
    suspend fun setGameArgs(boardN: Int, playerName: String)
}