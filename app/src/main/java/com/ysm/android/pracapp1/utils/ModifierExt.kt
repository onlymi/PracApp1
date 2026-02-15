package com.ysm.android.pracapp1.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.verticalScrollbar(
    state: ScrollState,
    width: Dp = 4.dp,
    color: Color = Color.Gray.copy(alpha = 0.5f)
): Modifier = composed {
    val targetAlpha = if (state.isScrollInProgress) 0.8f else 0.2f
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 500),
        label = "scrollbarAlpha"
    )

    drawWithContent {
        drawContent()

        val height = size.height
        val contentHeight = state.maxValue + height
        val scrollProgress = if (state.maxValue > 0) state.value.toFloat() / state.maxValue else 0f

        val calculatedHeight = (height / contentHeight) * height
        val scrollbarHeight = calculatedHeight.coerceAtMost(100.dp.toPx())
        val scrollableTrackHeight = height - scrollbarHeight
        val scrollbarYOffset = scrollProgress * scrollableTrackHeight

        drawRect(
            color = color,
            topLeft = Offset(x = size.width - width.toPx(), y = scrollbarYOffset),
            size = Size(width = width.toPx(), height = scrollbarHeight),
            alpha = alpha
        )
    }
}