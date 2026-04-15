package br.com.claus.sellvia.features.product.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.features.category.presentation.ListCategoryViewModel
import br.com.claus.sellvia.features.product.presentation.component.PlaceholderStep
import br.com.claus.sellvia.features.product.presentation.component.ProductStepper
import br.com.claus.sellvia.features.product.presentation.component.StepOneContent
import br.com.claus.sellvia.features.product.presentation.component.StepThreeContent
import br.com.claus.sellvia.features.product.presentation.component.StepTwoContent
import br.com.claus.sellvia.features.product.presentation.component.StepperNavigation
import br.com.claus.sellvia.features.product.presentation.component.SubmitResultDialog
import org.koin.androidx.compose.koinViewModel

data class StepperItem(
    val icon: ImageVector,
    val label: String
)

val registrySteps = listOf(
    StepperItem(Icons.Outlined.Edit, "Informações"),
    StepperItem(Icons.Outlined.Payments, "Preços"),
    StepperItem(Icons.Outlined.Inventory2, "Estoque"),
)

@Composable
fun RegistryProductScreen(
    bottomBarPadding: Dp = 0.dp,
    modifier: Modifier = Modifier,
    viewModel: RegistryProductViewModel = koinViewModel(),
    onNavigateToList: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditing = uiState.data.id != null
    var currentStep by remember { mutableIntStateOf(0) }

    val categoryViewModel: ListCategoryViewModel = koinViewModel()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onImageSelected(uri)
        }
    }

    uiState.submitResult?.let { result ->
        SubmitResultDialog(
            result = result,
            onDismiss = { viewModel.dismissSubmitResult() },
            onGoToList = {
                viewModel.dismissSubmitResult()
                onNavigateToList()
            },
            onRegisterNew = {
                viewModel.dismissSubmitResult()
                viewModel.resetForm()
                currentStep = 0
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = bottomBarPadding)
    ) {
        RegistryProductHeader(isEditing = isEditing)

        ProductStepper(
            steps = registrySteps,
            currentStep = currentStep,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        AnimatedContent(
            targetState = currentStep,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally(tween(300)) { it } + fadeIn(tween(300)) togetherWith
                            slideOutHorizontally(tween(300)) { -it } + fadeOut(tween(300))
                } else {
                    slideInHorizontally(tween(300)) { -it } + fadeIn(tween(300)) togetherWith
                            slideOutHorizontally(tween(300)) { it } + fadeOut(tween(300))
                }
            },
            label = "step_content"
        ) { step ->
            when (step) {
                0 -> StepOneContent(
                    uiState = uiState,
                    onNameChange = { viewModel.onNameChange(it) },
                    onDescriptionChange = { viewModel.onDescriptionChange(it) },
                    onSkuChange = { viewModel.onSkuChange(it) },
                    onImageClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onImageRemove = { viewModel.onImageSelected(null) },
                    onCategorySelected = { viewModel.onCategorySelected(it) },
                    categoryViewModel = categoryViewModel,
                    onExternalLinkChange = { viewModel.onExternalLinkChange(it) },
                    onWhatsappMessageChange = { viewModel.onWhatsappMessageChange(it) },
                )

                1 -> StepTwoContent(
                    uiState = uiState,
                    onPriceChange = viewModel::onPriceChange,
                    onCostChange = viewModel::onCostChange
                )

                2 -> StepThreeContent(
                    uiState = uiState,
                    onStockControlChange = viewModel::onStockControlChange,
                    onStockChange = viewModel::onStockChange
                )

                else -> PlaceholderStep(step = step + 1)
            }
        }

        StepperNavigation(
            currentStep = currentStep,
            totalSteps = registrySteps.size,
            onBack = { if (currentStep > 0) currentStep-- },
            onNext = {
                if (viewModel.validateStep(currentStep)) {
                    if (currentStep < registrySteps.size - 1) currentStep++
                }
            },
            onFinish = {
                if (viewModel.validateStep(currentStep)) viewModel.submit()
            },
            isLoading = viewModel.isLoading().collectAsState().value,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun RegistryProductHeader(isEditing: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
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
                imageVector = if (isEditing) Icons.Outlined.Edit else Icons.Outlined.Inventory,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Column {
            Text(
                text = if (isEditing) "Editar Produto" else "Novo Produto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (isEditing) "Atualize as informações do produto" else "Preencha os dados do seu produto",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
            )
        }
    }
}