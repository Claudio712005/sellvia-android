package br.com.claus.sellvia.features.registryProduct.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.features.registryProduct.presentation.RegistryProductUiState
import br.com.claus.sellvia.ui.components.imagePicker.ImagePickerBox

@Composable
fun StepOneContent(
    uiState: RegistryProductUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSkuChange: (String) -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { StepSectionLabel(label = "Imagem do Produto") }

        item {
            ImagePickerBox(
                imageUrl = uiState.data.imageUrl,
                onClick = onImageClick
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
                singleLine = true
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
                supportingText = "Código único de identificação do produto"
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
                maxLines = 6
            )
        }
    }
}
