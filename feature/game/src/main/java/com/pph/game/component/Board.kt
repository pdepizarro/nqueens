package com.pph.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pph.shared.model.CellPosUi
import com.pph.shared.testing.TestTags


@Composable
fun Board(
    n: Int,
    queens: Set<CellPosUi>,
    conflictLine: Set<CellPosUi>,
    onCellTap: (row: Int, col: Int) -> Unit,
    onBoardTap: () -> Unit,
    lightSquare: Color = Color(0xFFEEEED2),
    darkSquare: Color = Color(0xFF769656),
    boardCornerRadius: Dp = 8.dp
) {
    val conflictOverlay = Color(0xFFB00020).copy(alpha = 0.38f)

    Surface(
        shape = RoundedCornerShape(boardCornerRadius),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .testTag(TestTags.GAME_BOARD)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .pointerInput(n) { detectTapGestures { onBoardTap() } }
        ) {
            val squareSize = maxWidth / n

            Column(Modifier.fillMaxSize()) {
                for (row in 0 until n) {
                    Row(Modifier.fillMaxWidth()) {
                        for (col in 0 until n) {
                            val isDark = (row + col) % 2 == 1
                            val bg = if (isDark) darkSquare else lightSquare

                            val showNumber = (col == 0)
                            val showLetter = (row == n - 1)

                            val numberText = (n - row).toString()
                            val letterText = ('a'.code + col).toChar().toString()

                            val pos = CellPosUi(row, col)
                            val hasQueen = pos in queens
                            val isConflict = pos in conflictLine

                            Box(
                                modifier = Modifier
                                    .size(squareSize)
                                    .background(bg)
                                    .testTag(TestTags.boardCell(row, col))
                                    .pointerInput(row, col, hasQueen) {
                                        detectTapGestures {
                                            onCellTap(row, col)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isConflict) {
                                    Box(
                                        Modifier
                                            .matchParentSize()
                                            .background(conflictOverlay)
                                    )
                                }

                                if (showNumber) {
                                    CoordLabel(
                                        text = numberText,
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(4.dp),
                                        onDark = isDark
                                    )
                                }
                                if (showLetter) {
                                    CoordLabel(
                                        text = letterText,
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(4.dp),
                                        onDark = isDark
                                    )
                                }

                                QueenPiece(
                                    visible = hasQueen,
                                    squareSize = squareSize,
                                    emphasize = isConflict
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CoordLabel(
    text: String,
    modifier: Modifier = Modifier,
    onDark: Boolean
) {
    val color = if (onDark) Color(0xFFEFEFEF) else Color(0xFF1E1E1E)

    Text(
        text = text,
        color = color.copy(alpha = 0.85f),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}