package br.com.claus.sellvia.features.registryProduct.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.data.remote.model.response.CategoryResponse
import br.com.claus.sellvia.features.listCategory.presentation.ListCategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerBottomSheet(
    selectedCategory: CategoryResponse?,
    onCategorySelected: (CategoryResponse?) -> Unit,
    onDismiss: () -> Unit,
    categoryViewModel: ListCategoryViewModel,
) {
    val uiState by categoryViewModel.uiState.collectAsState()
    val currentPage by categoryViewModel.currentPage.collectAsState()
    val nameFilter by categoryViewModel.nameFilter.collectAsState()
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val categories = uiState.paginationData?.items ?: emptyList()
    val totalPages = uiState.paginationData?.totalPages ?: 1

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Category,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Selecionar Categoria",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Opcional — classifique seu produto",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            OutlinedTextField(
                value = nameFilter,
                onValueChange = { categoryViewModel.updateNameFilter(it) },
                placeholder = { Text("Buscar categoria...") },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                    focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
                ),
                modifier = Modifier.fillMaxWidth()
            )

            CategoryItem(
                name = "Sem categoria",
                description = "Não vincular a nenhuma categoria",
                isSelected = selectedCategory == null,
                onClick = {
                    onCategorySelected(null)
                    onDismiss()
                }
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                categories.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhuma categoria encontrada",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.height(280.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(categories, key = { it.id ?: 0 }) { category ->
                            CategoryItem(
                                name = category.name ?: "—",
                                description = category.description,
                                isSelected = selectedCategory?.id == category.id,
                                onClick = {
                                    onCategorySelected(category)
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }

            if (totalPages > 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { categoryViewModel.fetchCategories(currentPage - 1) },
                        enabled = currentPage > 0
                    ) {
                        Icon(Icons.Outlined.ChevronLeft, contentDescription = "Anterior",
                            tint = if (currentPage > 0) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                    }

                    Text(
                        text = "${currentPage + 1} de $totalPages",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    IconButton(
                        onClick = { categoryViewModel.fetchCategories(currentPage + 1) },
                        enabled = currentPage < totalPages - 1
                    ) {
                        Icon(Icons.Outlined.ChevronRight, contentDescription = "Próxima",
                            tint = if (currentPage < totalPages - 1) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    name: String,
    description: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (isSelected)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    else
        MaterialTheme.colorScheme.surface

    val borderColor = if (isSelected)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    else
        MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        } else {
            Spacer(Modifier.width(22.dp))
        }
    }
}