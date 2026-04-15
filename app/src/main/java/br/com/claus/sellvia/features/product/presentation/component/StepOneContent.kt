package br.com.claus.sellvia.features.product.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.features.category.domain.model.Category
import br.com.claus.sellvia.features.category.presentation.ListCategoryViewModel
import br.com.claus.sellvia.features.product.presentation.RegistryProductUiState
import br.com.claus.sellvia.ui.components.imagePicker.ImagePickerBox

@Composable
fun StepOneContent(
    uiState: RegistryProductUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSkuChange: (String) -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCategorySelected: (Category?) -> Unit,
    categoryViewModel: ListCategoryViewModel,
    onImageRemove: () -> Unit,
    onExternalLinkChange: (String) -> Unit,
    onWhatsappMessageChange: (String) -> Unit,
) {
    var showCategoryPicker by remember { mutableStateOf(false) }

    if (showCategoryPicker) {
        CategoryPickerBottomSheet(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = onCategorySelected,
            onDismiss = { showCategoryPicker = false },
            categoryViewModel = categoryViewModel
        )
    }

    LazyColumn (
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { StepSectionLabel(label = "Imagem do Produto") }

        item {
            ImagePickerBox(
                imageUri = uiState.localImageUri,
                onClick = onImageClick,
                onRemove = onImageRemove
            )
        }

        item { StepSectionLabel(label = "Identificação") }

        item {
            ProductTextField(
                value = uiState.data.name,
                onValueChange = onNameChange,
                label = "Nome do produto",
                placeholder = "Ex: Camiseta Básica Branca",
                leadingIcon = Icons.Outlined.Tag,
                singleLine = true,
                error = uiState.fieldErrors.name
            )
        }

        item {
            ProductTextField(
                value = uiState.data.sku,
                onValueChange = onSkuChange,
                label = "SKU",
                placeholder = "Ex: CAM-BASIC-BRC-M",
                leadingIcon = Icons.Outlined.QrCode,
                singleLine = true,
                supportingText = "Código único de identificação do produto",
                error = uiState.fieldErrors.sku
            )
        }

        item { StepSectionLabel(label = "Categoria") }

        item {
            CategorySelectorField(
                selectedCategory = uiState.selectedCategory,
                onClick = { showCategoryPicker = true }
            )
        }

        item { StepSectionLabel(label = "Descrição") }

        item {
            ProductTextField(
                value = uiState.data.description,
                onValueChange = onDescriptionChange,
                label = "Descrição",
                placeholder = "Descreva o produto, materiais, características...",
                leadingIcon = Icons.Outlined.Description,
                singleLine = false,
                minLines = 4,
                maxLines = 6,
                error = uiState.fieldErrors.description
            )
        }

        item { StepSectionLabel(label = "Links e Contato (opcional)") }

        item {
            ProductTextField(
                value = uiState.data.externalLink ?: "",
                onValueChange = onExternalLinkChange,
                label = "Link externo",
                placeholder = "Ex: https://meusite.com/produto",
                leadingIcon = Icons.Outlined.Link,
                singleLine = true,
                supportingText = "URL de divulgação do produto (máx. 500 caracteres)",
                error = uiState.fieldErrors.externalLink
            )
        }

        item {
            ProductTextField(
                value = uiState.data.whatsappMessage ?: "",
                onValueChange = onWhatsappMessageChange,
                label = "Mensagem do WhatsApp",
                placeholder = "Ex: Olá, tenho interesse no produto...",
                leadingIcon = Icons.Outlined.Message,
                singleLine = false,
                minLines = 3,
                maxLines = 5,
                supportingText = "Mensagem padrão para contato via WhatsApp"
            )
        }
    }
}

@Composable
private fun CategorySelectorField(
    selectedCategory: Category?,
    onClick: () -> Unit,
) {
    val label = selectedCategory?.name ?: "Selecionar categoria (opcional)"
    val hasSelection = selectedCategory != null

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.04f))
            .border(
                width = 1.dp,
                color = if (hasSelection)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                else
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Category,
            contentDescription = null,
            tint = if (hasSelection) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (hasSelection) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}