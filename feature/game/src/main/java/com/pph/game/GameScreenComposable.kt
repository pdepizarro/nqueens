package com.pph.game

import androidx.compose.runtime.Composable
import com.pph.uinavigation.ScreenComposable

class GameScreenComposable(
    private val onBack: () -> Unit
) : ScreenComposable {

    @Composable
    override fun Create() {
        GameScreen(
            onBack = onBack,
        )
    }
}