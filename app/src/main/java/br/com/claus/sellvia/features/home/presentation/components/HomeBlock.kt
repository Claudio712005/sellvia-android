package br.com.claus.sellvia.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.ui.theme.LightSurface
import br.com.claus.sellvia.ui.theme.SellviaNotSelected
import br.com.claus.sellvia.ui.theme.SellviaPrimary

data class HomeBlockData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val highlighted: Boolean = false,
    val badge: String? = null,
    val onClick: () -> Unit,
)

@Composable
fun HomeBlock(
    data: HomeBlockData,
    modifier: Modifier = Modifier,
) {
    val isHighlighted = data.highlighted

    val containerColor = if (isHighlighted) {
        SellviaPrimary.copy(alpha = 0.08f)
    } else {
        LightSurface
    }

    val contentColor = if (isHighlighted) {
        SellviaPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val iconContainerColor = if (isHighlighted) {
        SellviaPrimary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val iconTint = if (isHighlighted) {
        SellviaPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        onClick = data.onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isHighlighted) 6.dp else 2.dp
        ),
        border = if (isHighlighted) {
            androidx.compose.foundation.BorderStroke(
                1.dp,
                SellviaPrimary.copy(alpha = 0.2f)
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = iconContainerColor,
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = data.icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.SemiBold,
                        color = contentColor,
                    )

                    data.badge?.let {
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (isHighlighted) {
                                    SellviaPrimary.copy(alpha = 0.15f)
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer
                                },
                                labelColor = if (isHighlighted) {
                                    SellviaPrimary
                                } else {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                },
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = data.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = if (isHighlighted) 0.85f else 0.7f),
                )
            }
        }
    }
}