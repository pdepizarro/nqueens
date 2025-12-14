package com.pph.shared.mappers

import com.pph.domain.model.CellPos
import com.pph.shared.model.CellPosUi

fun CellPos.toUI() = CellPosUi(row, col)

fun Set<CellPos>.toUI(): Set<CellPosUi> =
    this.map { it.toUI() }.toSet()

fun CellPosUi.toDomain() = CellPos(row, col)

fun Set<CellPosUi>.toDomain(): Set<CellPos> =
    this.map { it.toDomain() }.toSet()