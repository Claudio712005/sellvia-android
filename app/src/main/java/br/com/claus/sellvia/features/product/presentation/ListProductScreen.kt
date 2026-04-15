package br.com.claus.sellvia.features.product.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.core.utils.formatDouble
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.features.product.presentation.component.ProductTextField
import br.com.claus.sellvia.features.product.presentation.components.ProductItem
import br.com.claus.sellvia.ui.components.paginationTemplate.Paginator
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListProductScreen(
    modifier: Modifier = Modifier,
    bottomBarPadding: Dp = 0.dp,
    viewModel: ListProductsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(uiState.actionResult) {
        val result = uiState.actionResult ?: return@LaunchedEffect
        val message = when (result) {
            is SubmitResult.Success -> "Operação realizada com sucesso"
            is SubmitResult.Error -> result.message
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.dismissActionResult()
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erro: ${uiState.error}")
            }
        } else {
            Paginator(
                modifier = Modifier,
                bottomBarPadding = bottomBarPadding,
                paginationResponse = uiState.paginationData ?: Pagination(),
                isLoading = uiState.isLoading,
                sortOptions = viewModel.sortOptions,
                selectedSort = selectedSort,
                onSortChange = { viewModel.updateSort(it) },
                onPageChange = { viewModel.fetchCategories(it) },
                onRefresh = { viewModel.fetchCategories(0) },
                itemContent = { product ->
                    ProductItem(
                        product = product,
                        onClick = { viewModel.onSelectProduct(product) }
                    )
                },
                keySelector = { it.id }
            )
        }

    }

    if (uiState.selectedProduct != null) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onDismissSheet() },
            sheetState = sheetState,
        ) {
            if (!uiState.isEditMode) {
                ProductActionSheet(
                    product = uiState.selectedProduct!!,
                    onEdit = { viewModel.onEditRequest() },
                    onDelete = { viewModel.onDeleteRequest() },
                )
            } else {
                EditProductForm(
                    uiState = uiState,
                    viewModel = viewModel,
                )
            }
        }
    }

    if (uiState.showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissDeleteConfirm() },
            title = { Text("Excluir produto") },
            text = {
                Text(
                    "Tem certeza que deseja excluir \"${uiState.selectedProduct?.name}\"? " +
                            "Essa ação não pode ser desfeita."
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onConfirmDelete() }) {
                    Text("Excluir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissDeleteConfirm() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (uiState.showEditSaveConfirm) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissEditSaveConfirm() },
            title = { Text("Salvar alterações") },
            text = { Text("Confirmar as alterações no produto \"${uiState.selectedProduct?.name}\"?") },
            confirmButton = {
                TextButton(onClick = { viewModel.onConfirmEditSave() }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissEditSaveConfirm() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (uiState.showImageUpdateConfirm) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissImageUpdateConfirm() },
            title = { Text("Alterar imagem") },
            text = { Text("Confirmar a substituição da imagem do produto \"${uiState.selectedProduct?.name}\"?") },
            confirmButton = {
                TextButton(onClick = { viewModel.onConfirmImageUpdate() }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissImageUpdateConfirm() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ProductActionSheet(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "SKU ${product.sku}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onEdit,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Outlined.Edit, contentDescription = null)
            Spacer(Modifier.padding(horizontal = 6.dp))
            Text("Editar")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onDelete,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            )
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = null)
            Spacer(Modifier.padding(horizontal = 6.dp))
            Text("Excluir")
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun EditProductForm(
    uiState: ListProductUiState,
    viewModel: ListProductsViewModel,
) {
    val formData = uiState.editFormData ?: return
    val errors = uiState.editFieldErrors

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> viewModel.onEditImageSelected(uri) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Editar produto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        HorizontalDivider()

        Text(
            text = "Imagem do produto",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        val imageModel: Any? = uiState.editImageUri ?: uiState.selectedProduct?.imageUrl
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.06f))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(14.dp)
                )
        ) {
            if (imageModel != null) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = "Imagem do produto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.85f))
                    .clickable {
                        imagePickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddPhotoAlternate,
                    contentDescription = "Alterar imagem",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        if (uiState.editImageUri != null) {
            Button(
                onClick = { viewModel.onImageUpdateRequest() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isUploadingImage,
            ) {
                if (uiState.isUploadingImage) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("Salvar nova imagem")
                }
            }
        }

        HorizontalDivider()

        ProductTextField(
            value = formData.name,
            onValueChange = { viewModel.onEditNameChange(it) },
            label = "Nome do produto",
            placeholder = "Ex: Camiseta Básica Branca",
            leadingIcon = Icons.Outlined.Tag,
            singleLine = true,
            error = errors.name,
        )

        ProductTextField(
            value = formData.sku,
            onValueChange = { viewModel.onEditSkuChange(it) },
            label = "SKU",
            placeholder = "Ex: CAM-BASIC-BRC-M",
            leadingIcon = Icons.Outlined.QrCode,
            singleLine = true,
            supportingText = "Código único de identificação",
            error = errors.sku,
        )

        ProductTextField(
            value = formData.description,
            onValueChange = { viewModel.onEditDescriptionChange(it) },
            label = "Descrição",
            placeholder = "Descreva o produto...",
            leadingIcon = Icons.Outlined.Description,
            singleLine = false,
            minLines = 3,
            maxLines = 5,
            error = errors.description,
        )

        ProductTextField(
            value = formatDouble(formData.price),
            onValueChange = { viewModel.onEditPriceChange(it) },
            label = "Preço",
            placeholder = "Ex: 99.90",
            leadingIcon = Icons.Outlined.AttachMoney,
            singleLine = true,
            keyboardType = KeyboardType.Number,
            error = errors.price,
        )

        ProductTextField(
            value = formatDouble(formData.productionCost),
            onValueChange = { viewModel.onEditCostChange(it) },
            label = "Custo de produção",
            placeholder = "Ex: 40.00",
            leadingIcon = Icons.Outlined.MoneyOff,
            singleLine = true,
            keyboardType = KeyboardType.Number,
            error = errors.productionCost,
        )

        val stockEnabled = formData.stockQuantity != null
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Outlined.Inventory,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                )
                Text("Controlar estoque", style = MaterialTheme.typography.bodyMedium)
            }
            Switch(
                checked = stockEnabled,
                onCheckedChange = { viewModel.onEditStockControlChange(it) },
            )
        }

        if (stockEnabled) {
            ProductTextField(
                value = formData.stockQuantity?.toString() ?: "",
                onValueChange = { viewModel.onEditStockChange(it) },
                label = "Quantidade em estoque",
                placeholder = "Ex: 50",
                leadingIcon = Icons.Outlined.Inventory,
                singleLine = true,
                keyboardType = KeyboardType.Number,
                error = errors.stockQuantity,
            )
        }

        Text(
            text = "Links e Contato (opcional)",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        ProductTextField(
            value = formData.externalLink ?: "",
            onValueChange = { viewModel.onEditExternalLinkChange(it) },
            label = "Link externo",
            placeholder = "Ex: https://meusite.com/produto",
            leadingIcon = Icons.Outlined.Link,
            singleLine = true,
            supportingText = "URL de divulgação do produto (máx. 500 caracteres)",
            error = errors.externalLink,
        )

        ProductTextField(
            value = formData.whatsappMessage ?: "",
            onValueChange = { viewModel.onEditWhatsappMessageChange(it) },
            label = "Mensagem do WhatsApp",
            placeholder = "Ex: Olá, tenho interesse no produto...",
            leadingIcon = Icons.Outlined.Message,
            singleLine = false,
            minLines = 3,
            maxLines = 5,
            supportingText = "Mensagem padrão para contato via WhatsApp",
        )

        Button(
            onClick = { viewModel.onEditSaveRequest() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Salvar alterações")
        }

        Spacer(Modifier.height(8.dp))
    }
}
