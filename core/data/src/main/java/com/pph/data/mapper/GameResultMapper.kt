package com.pph.data.mapper

import com.pph.data.model.entity.GameResultEntity
import com.pph.domain.model.GameResult

fun GameResult.toEntity() = GameResultEntity(
    id = id,
    playerName = playerName,
    boardN = boardN,
    elapsedMillis = elapsedMillis,
    finishedAtEpochMillis = finishedAtEpochMillis
)

fun GameResultEntity.toDomain() = GameResult(
    id = id,
    playerName = playerName,
    boardN = boardN,
    elapsedMillis = elapsedMillis,
    finishedAtEpochMillis = finishedAtEpochMillis
)