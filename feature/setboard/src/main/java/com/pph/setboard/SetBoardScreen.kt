package com.pph.setboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pph.setboard.components.ScoresPanel
import com.pph.setboard.event.SetBoardScreenEvent
import com.pph.shared.testing.TestTags
import com.pph.uicomponents.components.NumberDropdown

@Composable
fun SetBoardScreen(
    onNavigateNext: () -> Unit
) {
    val vm: SetBoardViewModel = hiltViewModel()
    val uiState by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.navigateNextEvent.collect { event ->
            when (event) {
                is SetBoardScreenEvent.NavigateNext -> onNavigateNext()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.set_board_field_n_queens),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.testTag(TestTags.SETBOARD_TITLE)
            )

            OutlinedTextField(
                value = uiState.playerName,
                onValueChange = vm::onPlayerNameChanged,
                singleLine = true,
                label = { Text("Player Name") },
                isError = uiState.showErrors && !uiState.isPlayerNameValid,
                modifier = Modifier.testTag(TestTags.SETBOARD_PLAYER_FIELD)
            )

            if (uiState.showErrors && !uiState.isPlayerNameValid) {
                Text(
                    text = "Please enter a name",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag(TestTags.SETBOARD_PLAYER_ERROR)
                )
            }

            NumberDropdown(
                label = stringResource(R.string.set_board_field_size_range_n),
                options = (4..9).toList(),
                selectedValue = uiState.boardN,
                onValueChange = vm::onBoardNChanged,
                isError = uiState.showErrors && !uiState.isNValid,
                modifier = Modifier.testTag(TestTags.SETBOARD_N_DROPDOWN),
                testTagPrefix = TestTags.SETBOARD_N_DROPDOWN
            )

            ScoresPanel(
                scores = uiState.scores,
                boardN = uiState.boardN,
                isInputValid = uiState.isNValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag(TestTags.SCORES_PANEL)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            ) {
                Button(
                    onClick = vm::onRandomPositionClick,
                    modifier = Modifier.testTag(TestTags.SETBOARD_RANDOM_BTN)
                ) {
                    Text(stringResource(R.string.set_board_btn_random_n))
                }

                Button(
                    onClick = vm::onSubmitClick,
                    enabled = uiState.canStart,
                    modifier = Modifier.testTag(TestTags.SETBOARD_PLAY_BTN)
                ) {
                    Text(stringResource(R.string.set_board_btn_lets_go))
                }
            }
        }
    }
}