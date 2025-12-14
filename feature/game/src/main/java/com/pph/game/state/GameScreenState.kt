package com.pph.game.state

import com.pph.shared.model.CellPosUi

data class GameScreenState(
    val boardN: Int = 8,
    val running: Boolean = false,
    val elapsedMillis: Long = 0L,
    val queens: Set<CellPosUi> = emptySet(),
    val conflictLine: Set<CellPosUi> = emptySet(),
    val showWinDialog: Boolean = false,
    val isNewRecord: Boolean = false,
    val playerName: String = "",
    val bestTimeMillis: Long? = null

) {
    val queensCount: Int get() = queens.size
}