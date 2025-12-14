package com.pph.nqueens.testing

import android.content.Context
import java.io.File

object TestStorage {
    fun clearGamePrefsDataStore(context: Context) {
        val dsDir = File(context.filesDir, "datastore")
        val dsFile = File(dsDir, "game_prefs.preferences_pb")
        if (dsFile.exists()) dsFile.delete()
    }
}
