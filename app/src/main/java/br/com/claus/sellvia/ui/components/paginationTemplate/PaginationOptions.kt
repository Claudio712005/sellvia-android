package br.com.claus.sellvia.ui.components.paginationTemplate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.core.ui.extensions.toItemLabel
import br.com.claus.sellvia.ui.components.paginationTemplate.models.SortOption

@Composable
fun PaginationOptions(
    currentPage: Int,
    totalPages: Int,
    totalItems: Long,
    sortOptions: List<SortOption>,
    selectedSort: SortOption,
    onPageChange: (Int) -> Unit,
    onSortChange: (SortOption) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    val uiPage = currentPage
    val apiPage = uiPage - 1

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = buildPageLabel(currentPage, totalPages, isLoading),
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

                    FilledIconButton(
                        onClick = { onPageChange(apiPage - 1) },
                        enabled = !isLoading && uiPage > 1,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, null)
                    }

                    FilledIconButton(
                        onClick = { onPageChange(apiPage + 1) },
                        enabled = !isLoading && uiPage < totalPages,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.ArrowForwardIos, null)
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SortDropdown(
                            sortOptions = sortOptions,
                            selectedSort = selectedSort,
                            onSortChange = onSortChange,
                            enabled = !isLoading
                        )

                        Text(
                            text = if (isLoading) "Carregando..." else totalItems.toItemLabel(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Text(
                text = if (expanded) "Ocultar opções" else "Mais opções",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
                    .clickable { expanded = !expanded },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

fun buildPageLabel(
    currentPage: Int,
    totalPages: Int,
    isLoading: Boolean
): String = when {
    isLoading -> "Carregando..."
    totalPages <= 0 -> "Página 1 de 1"
    else -> {
        val safePage = (currentPage).coerceIn(1, totalPages)
        "Página $safePage de $totalPages"
    }
}
