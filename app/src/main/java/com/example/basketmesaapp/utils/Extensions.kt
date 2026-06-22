package com.example.basketmesaapp.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.fadingEdge(state: LazyListState): Modifier {
    val bgColor = MaterialTheme.colorScheme.background
    return this.drawWithContent {
        drawContent()
        val fadeHeight = 40.dp.toPx()

        if (state.canScrollBackward) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(bgColor, Color.Transparent),
                    startY = 0f,
                    endY = fadeHeight
                ),
                size = size.copy(height = fadeHeight)
            )
        }
        if (state.canScrollForward) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, bgColor),
                    startY = size.height - fadeHeight,
                    endY = size.height
                ),
                topLeft = Offset(0f, size.height - fadeHeight),
                size = size.copy(height = fadeHeight)
            )
        }
    }
}