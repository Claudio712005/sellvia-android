package br.com.claus.sellvia.features.login.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.claus.sellvia.core.theme.SellviaBlue

@Composable
fun BackToLoginButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) +
                slideInVertically(
                    initialOffsetY = { -20 },
                    animationSpec = tween(400)
                ),
        exit = fadeOut(tween(200))

    ) {
        Row(
            modifier = Modifier
                .padding(top = 30.dp, start = 16.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Voltar",
                tint = SellviaBlue
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Voltar",
                color = SellviaBlue,
                fontSize = 14.sp
            )
        }
    }
}