package br.com.claus.sellvia.features.catalog.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.core.model.CardStyle
import br.com.claus.sellvia.core.model.Direction
import br.com.claus.sellvia.features.catalog.domain.model.CatalogDisplayOptions
import br.com.claus.sellvia.features.catalog.domain.model.CatalogFilterOptions
import br.com.claus.sellvia.features.catalog.presentation.CatalogUiState
import br.com.claus.sellvia.features.catalog.presentation.DownloadStatus
import br.com.claus.sellvia.features.product.domain.model.ProductType

private val sortOptions = listOf(
    "name" to "Nome",
    "price" to "Preço",
    "sku" to "SKU",
    "createdAt" to "Data de criação",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogBottomSheet(
    uiState: CatalogUiState,
    onDismiss: () -> Unit,
    onDisplayOptionsUpdate: (CatalogDisplayOptions.() -> CatalogDisplayOptions) -> Unit,
    onFilterOptionsUpdate: (CatalogFilterOptions.() -> CatalogFilterOptions) -> Unit,
    onDownload: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableIntStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 4.dp),
            ) {
                Text(
                    text = "Configurar Catálogo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "Personalize filtros e opções de exibição do PDF",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(8.dp))

            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Filtros") },
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Exibição") },
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (selectedTab == 0) {
                    FiltersTab(
                        filter = uiState.filterOptions,
                        onUpdate = onFilterOptionsUpdate,
                    )
                } else {
                    DisplayTab(
                        display = uiState.displayOptions,
                        onUpdate = onDisplayOptionsUpdate,
                    )
                }

                Spacer(Modifier.height(20.dp))

                val isDownloading = uiState.downloadStatus is DownloadStatus.InProgress
                Button(
                    onClick = onDownload,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isDownloading,
                ) {
                    if (isDownloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Iniciando download…")
                    } else {
                        Icon(Icons.Outlined.Download, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Baixar Catálogo")
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FiltersTab(
    filter: CatalogFilterOptions,
    onUpdate: (CatalogFilterOptions.() -> CatalogFilterOptions) -> Unit,
) {
    var nameText by remember(filter.name) { mutableStateOf(filter.name.orEmpty()) }
    var skuText by remember(filter.sku) { mutableStateOf(filter.sku.orEmpty()) }
    var categoryText by remember(filter.categoryId) {
        mutableStateOf(filter.categoryId?.toString().orEmpty())
    }
    var minPriceText by remember(filter.minPrice) {
        mutableStateOf(filter.minPrice?.toString().orEmpty())
    }
    var maxPriceText by remember(filter.maxPrice) {
        mutableStateOf(filter.maxPrice?.toString().orEmpty())
    }

    SectionLabel("Busca de produtos")
    FilterTextField(
        value = nameText,
        onValueChange = {
            nameText = it
            onUpdate { copy(name = it.ifBlank { null }) }
        },
        label = "Nome do produto",
    )
    Spacer(Modifier.height(8.dp))
    FilterTextField(
        value = skuText,
        onValueChange = {
            skuText = it
            onUpdate { copy(sku = it.ifBlank { null }) }
        },
        label = "SKU",
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done,
    )

    SectionDivider()

    SectionLabel("Categoria")
    FilterTextField(
        value = categoryText,
        onValueChange = {
            categoryText = it
            onUpdate { copy(categoryId = it.trim().toLongOrNull()) }
        },
        label = "ID da categoria",
        keyboardType = KeyboardType.Number,
    )

    SectionDivider()

    SectionLabel("Faixa de preço")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterTextField(
            value = minPriceText,
            onValueChange = {
                minPriceText = it
                onUpdate { copy(minPrice = it.trim().toDoubleOrNull()) }
            },
            label = "Preço mínimo",
            keyboardType = KeyboardType.Decimal,
            modifier = Modifier.weight(1f),
        )
        FilterTextField(
            value = maxPriceText,
            onValueChange = {
                maxPriceText = it
                onUpdate { copy(maxPrice = it.trim().toDoubleOrNull()) }
            },
            label = "Preço máximo",
            keyboardType = KeyboardType.Decimal,
            modifier = Modifier.weight(1f),
        )
    }

    SectionDivider()

    SectionLabel("Status")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(null to "Todos", true to "Ativos", false to "Inativos").forEach { (value, label) ->
            FilterChip(
                selected = filter.active == value,
                onClick = { onUpdate { copy(active = value) } },
                label = { Text(label) },
            )
        }
    }

    SectionDivider()

    SectionLabel("Tipo de produto")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ChipOption(label = "Todos", selected = filter.type == null) {
            onUpdate { copy(type = null) }
        }
        ProductType.entries.forEach { type ->
            ChipOption(
                label = type.label,
                selected = filter.type == type,
            ) { onUpdate { copy(type = type) } }
        }
    }

    SectionDivider()

    SectionLabel("Ordenar por")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        sortOptions.forEach { (value, label) ->
            ChipOption(
                label = label,
                selected = (filter.sort ?: "name") == value,
            ) { onUpdate { copy(sort = value) } }
        }
    }

    Spacer(Modifier.height(8.dp))
    SectionLabel("Direção")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ChipOption(
            label = "A → Z  /  Menor → Maior",
            selected = (filter.direction ?: Direction.ASC) == Direction.ASC,
        ) { onUpdate { copy(direction = Direction.ASC) } }
        ChipOption(
            label = "Z → A  /  Maior → Menor",
            selected = filter.direction == Direction.DESC,
        ) { onUpdate { copy(direction = Direction.DESC) } }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DisplayTab(
    display: CatalogDisplayOptions,
    onUpdate: (CatalogDisplayOptions.() -> CatalogDisplayOptions) -> Unit,
) {
    SectionLabel("Layout")

    Text(
        text = "Colunas por linha",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 4.dp),
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        (1..4).forEach { cols ->
            ChipOption(
                label = cols.toString(),
                selected = display.columnsPerRow == cols,
            ) { onUpdate { copy(columnsPerRow = cols) } }
        }
    }

    Spacer(Modifier.height(8.dp))

    Text(
        text = "Estilo do card",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 4.dp),
    )
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CardStyle.entries.forEach { style ->
            ChipOption(
                label = style.label,
                selected = display.cardStyle == style,
            ) { onUpdate { copy(cardStyle = style) } }
        }
    }

    SectionDivider()

    SectionLabel("Imagens")
    OptionSwitch("Imagens dos produtos", display.showProductImages) {
        onUpdate { copy(showProductImages = it) }
    }

    SectionDivider()

    SectionLabel("Empresa")
    OptionSwitch("Logo da empresa", display.showCompanyLogo) {
        onUpdate { copy(showCompanyLogo = it) }
    }
    OptionSwitch("Razão social", display.showCompanyBusinessName) {
        onUpdate { copy(showCompanyBusinessName = it) }
    }
    OptionSwitch("CNPJ", display.showCompanyCnpj) {
        onUpdate { copy(showCompanyCnpj = it) }
    }
    OptionSwitch("Site da empresa", display.showCompanyWebsite) {
        onUpdate { copy(showCompanyWebsite = it) }
    }

    SectionDivider()

    SectionLabel("Estatísticas")
    OptionSwitch("Painel de estatísticas", display.showStats) {
        onUpdate { copy(showStats = it) }
    }
    OptionSwitch("Preço médio", display.showAveragePrice) {
        onUpdate { copy(showAveragePrice = it) }
    }
    OptionSwitch("Contagem de ativos", display.showActiveCount) {
        onUpdate { copy(showActiveCount = it) }
    }
    OptionSwitch("Contagem por tipo", display.showTypeBreakdown) {
        onUpdate { copy(showTypeBreakdown = it) }
    }

    SectionDivider()

    SectionLabel("Card do Produto")
    OptionSwitch("SKU", display.showSku) {
        onUpdate { copy(showSku = it) }
    }
    OptionSwitch("Estoque", display.showStock) {
        onUpdate { copy(showStock = it) }
    }
    OptionSwitch("Custo de produção", display.showProductionCost) {
        onUpdate { copy(showProductionCost = it) }
    }
    OptionSwitch("Categoria", display.showCategory) {
        onUpdate { copy(showCategory = it) }
    }
    OptionSwitch("Descrição", display.showDescription) {
        onUpdate { copy(showDescription = it) }
    }
    OptionSwitch("Status (ativo/inativo)", display.showStatus) {
        onUpdate { copy(showStatus = it) }
    }
}

@Composable
private fun SectionLabel(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
    )
}

@Composable
private fun SectionDivider() {
    Spacer(Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun OptionSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun ChipOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    )
}

@Composable
private fun FilterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

private val ProductType.label: String
    get() = when (this) {
        ProductType.PHYSICAL -> "Físico"
        ProductType.DIGITAL -> "Digital"
        ProductType.SERVICE -> "Serviço"
    }
