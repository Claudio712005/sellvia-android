package br.com.claus.sellvia.features.splash.presentation

import PoweredByClaus
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.R
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    companyLogoUrl: String?,
    onFinished: () -> Unit
) {
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, tween(800))
        alpha.animateTo(1f, tween(600))
        delay(700)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {

        if (companyLogoUrl == null) {
            Image(
                painter = painterResource(R.drawable.logo_sellvia_removebg),
                contentDescription = "Sellvia Logo",
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.Center)
                    .scale(scale.value)
                    .alpha(alpha.value)
            )
        } else {
            AsyncImage(
                model = companyLogoUrl,
                contentDescription = "Company Logo",
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.Center)
                    .scale(scale.value)
                    .alpha(alpha.value)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            PoweredByClaus()
        }
    }
}
