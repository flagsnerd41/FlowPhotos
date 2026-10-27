package com.flowphotos.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FlowDarkScheme = darkColorScheme(
    primary = FlowPrimary,
    secondary = FlowSecondary,
    tertiary = FlowTertiary,
    background = FlowBackground,
    surface = FlowSurface,
    surfaceVariant = FlowSurfaceVariant,
    onPrimary = Color(0xFF06101F),
    onSecondary = Color(0xFF04201C),
    onTertiary = Color(0xFF1A1030),
    onBackground = FlowOnBackground,
    onSurface = FlowOnSurface,
    onSurfaceVariant = FlowMuted,
)

@Composable
fun FlowPhotosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FlowDarkScheme,
        content = content,
    )
}
