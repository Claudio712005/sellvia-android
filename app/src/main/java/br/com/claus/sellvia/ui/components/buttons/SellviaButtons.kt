package br.com.claus.sellvia.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.claus.sellvia.core.theme.SellviaBlue

@Composable
fun SellviaPrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
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
fun SellviaSecondaryButton(text: String) {
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
