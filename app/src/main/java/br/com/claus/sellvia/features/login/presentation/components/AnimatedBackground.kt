package br.com.claus.sellvia.features.login.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import br.com.claus.sellvia.core.theme.SellviaBlueDark
import br.com.claus.sellvia.core.theme.SellviaCyan

@Composable
fun AnimatedCurvedBackground(
    expanded: Boolean
) {

    val infiniteTransition = rememberInfiniteTransition(label = "background")

    val offsetX by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetX"
    )

    val offsetY by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    val scale by animateFloatAsState(
        targetValue = if (expanded) 1.25f else 1f,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {

        val width = size.width
        val height = size.height

        drawCircle(
            color = SellviaCyan.copy(alpha = 0.25f),
            radius = width * 1.15f * scale,
            center = Offset(
                x = width / 2f + offsetX,
                y = height * 1.25f + offsetY
            )
        )

        drawCircle(
            color = SellviaBlueDark,
            radius = width * 1.0f * scale,
            center = Offset(
                x = width / 2f - offsetX,
                y = height * 1.35f - offsetY
            )
        )
    }
}
