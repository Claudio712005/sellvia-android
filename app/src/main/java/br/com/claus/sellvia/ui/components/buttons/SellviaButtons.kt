package br.com.claus.sellvia.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.claus.sellvia.ui.theme.SellviaPrimary

@Composable
fun SellviaPrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
){
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SellviaPrimary,
            disabledContainerColor = SellviaPrimary.copy(alpha = 0.5f)
        )
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun SellviaSecondaryButton(text: String, onClick: () -> Unit = {}) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, SellviaPrimary)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SellviaPrimary)
    }
}