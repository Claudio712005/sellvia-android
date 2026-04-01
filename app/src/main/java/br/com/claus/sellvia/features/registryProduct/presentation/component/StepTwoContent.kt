package br.com.claus.sellvia.features.registryProduct.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.data.utils.formatDouble
import br.com.claus.sellvia.features.registryProduct.presentation.RegistryProductUiState

@Composable
fun StepTwoContent(
    uiState: RegistryProductUiState,
    onPriceChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item { StepSectionLabel(label = "Preço de Venda") }

        item {
            ProductTextField(
                value = formatDouble(uiState.data.price),
                onValueChange = onPriceChange,
                label = "Preço",
                placeholder = "Ex: 99.90",
                leadingIcon = Icons.Outlined.AttachMoney,
                singleLine = true,
                error = uiState.fieldErrors.price
            )
        }

        item { StepSectionLabel(label = "Custo de Produção") }

        item {
            ProductTextField(
                value = formatDouble(uiState.data.productionCost),
                onValueChange = onCostChange,
                label = "Custo",
                placeholder = "Ex: 40.00",
                leadingIcon = Icons.Outlined.MoneyOff,
                singleLine = true,
                supportingText = "Quanto custa produzir ou adquirir este produto",
                error = uiState.fieldErrors.productionCost
            )
        }

        item { StepSectionLabel(label = "Resumo") }

        item {
            val price = uiState.data.price
            val cost = uiState.data.productionCost
            val profit = price - cost
            val margin = if (price > 0) (profit / price) * 100 else 0.0

            StepInfoRow(label = "Lucro", value = "R$ %.2f".format(profit))
            StepInfoRow(label = "Margem", value = "%.1f%%".format(margin))
        }
    }
}