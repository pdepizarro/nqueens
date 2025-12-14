package com.pph.setboard

import androidx.compose.runtime.Composable
import com.pph.uinavigation.ScreenComposable

class SetBoardScreenComposable(
    private val onNavigateNext: () -> Unit
) : ScreenComposable {

    @Composable
    override fun Create() {
        SetBoardScreen(
            onNavigateNext = onNavigateNext
        )
    }
}