package com.pph.shared.mappers

import com.pph.domain.model.GameResult
import com.pph.shared.model.GameResultUi

fun GameResult.toUi(): GameResultUi = GameResultUi(
    id = id,
    playerName = playerName,
    boardN = boardN,
    elapsedMillis = elapsedMillis,
    finishedAtEpochMillis = finishedAtEpochMillis
)

fun List<GameResult>.toUi(): List<GameResultUi> = this.map { it.toUi() }