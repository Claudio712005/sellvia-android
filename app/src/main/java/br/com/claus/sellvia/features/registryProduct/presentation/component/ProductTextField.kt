package br.com.claus.sellvia.features.registryProduct.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ProductTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    singleLine: Boolean,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = 1,
    supportingText: String? = null,
    error: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        },
        supportingText =
            if (error != null) {
                {
                    Text(
                        error,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                if (supportingText != null) {
                    { Text(supportingText, style = MaterialTheme.typography.labelSmall) }
                } else null
            },
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier.fillMaxWidth()
    )
}
