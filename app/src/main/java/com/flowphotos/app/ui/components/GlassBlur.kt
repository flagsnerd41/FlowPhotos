package com.flowphotos.app.ui.components

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Real backdrop-style blur on API 31+.
 * Falls back to translucent glass fill on older devices.
 */
fun Modifier.realBlur(radiusPx: Float = 28f): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.graphicsLayer {
            renderEffect = RenderEffect
                .createBlurEffect(radiusPx, radiusPx, Shader.TileMode.CLAMP)
                .asComposeRenderEffect()
        }
    } else {
        this
    }
}

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 0.dp,
    fill: Color = Color(0x6612121A),
    border: Color = Color(0x28FFFFFF),
    blurRadiusPx: Float = 32f,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .then(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Modifier.graphicsLayer {
                        renderEffect = RenderEffect
                            .createBlurEffect(blurRadiusPx, blurRadiusPx, Shader.TileMode.CLAMP)
                            .asComposeRenderEffect()
                    }
                } else {
                    Modifier
                },
            )
            .background(fill)
            .border(1.dp, border, shape),
        content = content,
    )
}
