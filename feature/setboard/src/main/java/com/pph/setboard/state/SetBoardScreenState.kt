package com.pph.setboard.state

import com.pph.shared.model.GameResultUi

data class SetBoardScreenState(
    val boardN: String = "5",
    val playerName: String = "",
    val showErrors: Boolean = false,
    val scores: List<GameResultUi> = emptyList()
) {

    val boardNInt: Int get() = boardN.toIntOrNull() ?: -1

    val isPlayerNameValid: Boolean get() = playerName.isNotBlank()
    val isNValid = boardNInt in 4..9

    val canStart: Boolean get() = isNValid && isPlayerNameValid
}