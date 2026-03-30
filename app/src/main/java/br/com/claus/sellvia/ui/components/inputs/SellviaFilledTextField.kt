package br.com.claus.sellvia.ui.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.claus.sellvia.ui.theme.SellviaPrimary
import br.com.claus.sellvia.ui.theme.SellviaSecondary
import br.com.claus.sellvia.ui.theme.SellviaTertiary

@Composable
fun SellviaFilledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        label = {
            Text(
                text = label,
                fontSize = 13.sp
            )
        },
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.96f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.96f),
            focusedIndicatorColor = SellviaPrimary,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = SellviaPrimary,
            unfocusedLabelColor = SellviaSecondary.copy(alpha = 0.7f),
            cursorColor = SellviaPrimary,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}
