package com.pph.data.local.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.gamePrefsDataStore: DataStore<Preferences> by preferencesDataStore(name = "game_prefs")

@Singleton
class GamePrefsDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private object Keys {
        val BOARD_N = intPreferencesKey("board_n")
        val PLAYER_NAME = stringPreferencesKey("player_name")
    }

    val boardNFlow: Flow<Int> = context.gamePrefsDataStore.data
        .map { prefs -> prefs[Keys.BOARD_N] ?: 8 }

    val playerNameFlow: Flow<String> = context.gamePrefsDataStore.data
        .map { prefs -> prefs[Keys.PLAYER_NAME].orEmpty() }

    suspend fun setGameArgs(boardN: Int, playerName: String) {
        context.gamePrefsDataStore.edit {
            it[Keys.BOARD_N] = boardN
            it[Keys.PLAYER_NAME] = playerName
        }
    }
}
