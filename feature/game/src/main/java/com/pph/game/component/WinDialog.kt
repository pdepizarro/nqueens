package com.pph.game.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pph.shared.testing.TestTags
import com.pph.shared.utils.formatTimeMillis
import com.pph.game.R

@Composable
fun WinDialog(
    elapsedMillis: Long,
    currentRecordMillis: Long?,
    isNewRecord: Boolean,
    onClose: () -> Unit,
    onBack: () -> Unit,
    onReset: () -> Unit
) {
    val timeText = formatTimeMillis(elapsedMillis)

    val infiniteTransition = rememberInfiniteTransition(label = "recordPulse")
    val recordScale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "recordScale"
    )

    BackHandler(enabled = true) { onBack() }

    AlertDialog(
        modifier = Modifier.testTag(TestTags.WIN_DIALOG),
        onDismissRequest = {},
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.win_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag(TestTags.WIN_TITLE)
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (isNewRecord) {
                    Text(
                        text = stringResource(R.string.win_dialog_new_record),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .scale(recordScale.value)
                            .testTag(TestTags.WIN_RECORD)
                    )
                } else {
                    currentRecordMillis?.let { best ->
                        val bestTimeText = formatTimeMillis(best)
                        Text(
                            text = stringResource(R.string.win_dialog_current_record, bestTimeText),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.testTag(TestTags.WIN_RECORD)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.win_dialog_your_time, timeText),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.testTag(TestTags.WIN_TIME)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onReset,
                modifier = Modifier.testTag(TestTags.WIN_PLAY_AGAIN)
            ) { Text(stringResource(R.string.win_dialog_btn_play_again)) }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClose()
                    onBack()
                },
                modifier = Modifier.testTag(TestTags.WIN_GO_BACK)
            ) { Text(stringResource(R.string.win_dialog_btn_go_back)) }
        }
    )
}
