package com.pph.game.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.pph.game.R


@Composable
fun QueenPiece(
    visible: Boolean,
    squareSize: Dp,
    emphasize: Boolean = false
) {
    val targetAlpha = if (visible) 1f else 0f
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "queenAlpha"
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(
            animationSpec = spring(
                dampingRatio = if (emphasize)
                    Spring.DampingRatioLowBouncy
                else
                    Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialScale = 0.6f
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.queen),
            contentDescription = "Queen",
            modifier = Modifier
                .size(squareSize * 0.72f)
                .alpha(alpha),
            tint = Color.Unspecified
        )
    }
}
