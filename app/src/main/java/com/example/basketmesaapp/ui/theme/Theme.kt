package com.example.basketmesaapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val AppColorScheme = darkColorScheme(
    primary = Color(0xFFFF8A65),
    onPrimary = Color(0xFF212121),
    secondary = Color(0xFF64B5F6),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF334155),
    onSurface = Color(0xFFF8FAFC)
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun BasketMesaAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        shapes = AppShapes,
        content = content
    )
}