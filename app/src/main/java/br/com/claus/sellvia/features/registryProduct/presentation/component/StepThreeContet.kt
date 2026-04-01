package br.com.claus.sellvia.features.registryProduct.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.features.registryProduct.presentation.RegistryProductUiState

@Composable
fun StepThreeContent(
    uiState: RegistryProductUiState,
    onStockControlChange: (Boolean) -> Unit,
    onStockChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hasStockControl = uiState.data.stockQuantity != null

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item { StepSectionLabel(label = "Controle de Estoque") }

        item {
            ProductSwitchRow(
                label = "Controlar estoque",
                checked = hasStockControl,
                onCheckedChange = onStockControlChange
            )
        }

        if (hasStockControl) {

            item { StepSectionLabel(label = "Quantidade em Estoque") }

            item {
                ProductTextField(
                    value = uiState.data.stockQuantity?.toString() ?: "",
                    onValueChange = onStockChange,
                    label = "Quantidade",
                    placeholder = "Ex: 100",
                    leadingIcon = Icons.Outlined.Inventory,
                    singleLine = true,
                    error = uiState.fieldErrors.stockQuantity
                )
            }
        }

        item { StepSectionLabel(label = "Resumo") }

        item {
            StepInfoRow(
                label = "Controle ativo",
                value = if (hasStockControl) "Sim" else "Não"
            )

            StepInfoRow(
                label = "Quantidade",
                value = uiState.data.stockQuantity?.toString() ?: "-"
            )
        }
    }
}