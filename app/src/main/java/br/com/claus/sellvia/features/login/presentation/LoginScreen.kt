package br.com.claus.sellvia.features.login.presentation

import PoweredByClaus
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.claus.sellvia.R
import br.com.claus.sellvia.core.theme.SellviaBackground
import br.com.claus.sellvia.core.theme.SellviaBlue
import br.com.claus.sellvia.core.theme.SellviaBlueDark
import br.com.claus.sellvia.core.theme.SellviaCyan

@Composable
fun LoginScreenContent() {

    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SellviaBackground)
    ) {

        AnimatedCurvedBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(56.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(tween(800)) +
                        slideInVertically(
                            initialOffsetY = { -40 },
                            animationSpec = tween(800)
                        )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_sellvia_removebg),
                    contentDescription = "Sellvia Logo",
                    modifier = Modifier.height(284.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(tween(700, delayMillis = 300)) +
                        slideInVertically(
                            initialOffsetY = { 40 },
                            animationSpec = tween(700, delayMillis = 300)
                        )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SellviaPrimaryButton(text = "Entrar")

                    Spacer(modifier = Modifier.height(12.dp))

                    SellviaSecondaryButton(text = "Criar conta")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(tween(600, delayMillis = 600))
            ) {
                PoweredByClaus()
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SellviaPrimaryButton(text: String) {
    Button(
        onClick = {},
        modifier = Modifier
            .width(220.dp)
            .height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SellviaBlue)
    ) {
        Text(text = text, fontSize = 15.sp, color = Color.White)
    }
}

@Composable
private fun SellviaSecondaryButton(text: String) {
    OutlinedButton(
        onClick = {},
        modifier = Modifier
            .width(220.dp)
            .height(48.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, SellviaBlue)
    ) {
        Text(text = text, fontSize = 15.sp, color = SellviaBlue)
    }
}

@Composable
private fun AnimatedCurvedBackground() {

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

    Canvas(modifier = Modifier.fillMaxSize()) {

        val width = size.width
        val height = size.height

        drawCircle(
            color = SellviaCyan.copy(alpha = 0.25f),
            radius = width * 1.15f,
            center = Offset(
                x = width / 2f + offsetX,
                y = height * 1.25f + offsetY
            )
        )

        drawCircle(
            color = SellviaBlueDark,
            radius = width * 1.0f,
            center = Offset(
                x = width / 2f - offsetX,
                y = height * 1.35f - offsetY
            )
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp"
)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent()
    }
}
