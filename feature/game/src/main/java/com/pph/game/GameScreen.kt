package com.pph.game

import com.pph.game.component.Header
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pph.game.component.Board
import com.pph.game.component.WinDialog
import com.pph.uicomponents.components.LoadingComponent

@Composable
fun GameScreen(
    onBack: () -> Unit,
) {
    val vm: GameViewModel = hiltViewModel()
    val uiState by vm.state.collectAsState()

    if (uiState.isLoading) {
        LoadingComponent()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Header(
                elapsedMillis = uiState.elapsedMillis,
                isRunning = uiState.running,
                queensCount = uiState.queensCount,
                n = uiState.boardN,
                onReset = vm::onResetClick
            )

            Spacer(modifier = Modifier.weight(1f))

            Board(
                n = uiState.boardN,
                queens = uiState.queens,
                conflictLine = uiState.conflictLine,
                onCellTap = vm::onCellTapped,
                onBoardTap = vm::onBoardTapped
            )

            Spacer(modifier = Modifier.weight(1.2f))
        }

        if (uiState.showWinDialog) {
            WinDialog(
                elapsedMillis = uiState.elapsedMillis,
                currentRecordMillis = uiState.bestTimeMillis,
                isNewRecord = uiState.isNewRecord,
                onClose = { vm.hideWinDialog() },
                onBack = onBack,
                onReset = vm::onResetClick
            )
        }
    }
}