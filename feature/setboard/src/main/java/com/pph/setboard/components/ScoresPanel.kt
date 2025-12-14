package com.pph.setboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pph.setboard.R
import com.pph.shared.model.GameResultUi
import com.pph.shared.testing.TestTags

@Composable
fun ScoresPanel(
    boardN: String,
    isInputValid: Boolean,
    scores: List<GameResultUi>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.score_panel_title, boardN),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag(TestTags.SCORES_TITLE)
            )

            when {
                !isInputValid -> {
                    EmptyStateCentered(stringResource(R.string.score_panel_empty_state_n_not_selected))
                }

                scores.isEmpty() -> {
                    EmptyStateCentered(stringResource(R.string.score_panel_empty_state_no_data))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(TestTags.SCORES_LIST),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(count = scores.size) { index ->
                            val score = scores[index]
                            ScoreCell(
                                position = index + 1,
                                playerName = score.playerName,
                                elapsedMillis = score.elapsedMillis,
                                modifier = Modifier.testTag(TestTags.scoreItem(index + 1))
                            )
                        }
                    }
                }
            }
        }
    }
}