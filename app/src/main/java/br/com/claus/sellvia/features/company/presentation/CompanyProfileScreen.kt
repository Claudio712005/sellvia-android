package br.com.claus.sellvia.features.company.presentation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.claus.sellvia.core.model.UserRole
import br.com.claus.sellvia.ui.theme.LightSurface
import br.com.claus.sellvia.ui.theme.SellviaNotSelected
import br.com.claus.sellvia.ui.theme.SellviaPrimary
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun CompanyProfileScreen(
    userRole: UserRole?,
    modifier: Modifier = Modifier,
    bottomBarPadding: Dp = 0.dp,
    viewModel: CompanyViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.saveMessage) {
        val msg = uiState.saveMessage ?: return@LaunchedEffect
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        viewModel.dismissSaveMessage()
    }

    LaunchedEffect(uiState.imageMessage) {
        val msg = uiState.imageMessage ?: return@LaunchedEffect
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        viewModel.dismissImageMessage()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = bottomBarPadding),
        contentAlignment = Alignment.Center,
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator()

            uiState.error != null -> Text(
                text = "Erro: ${uiState.error}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(24.dp),
            )

            uiState.company != null -> CompanyEditContent(
                uiState = uiState,
                userRole = userRole,
                viewModel = viewModel,
            )
        }
    }
}

@Composable
private fun CompanyEditContent(
    uiState: CompanyUiState,
    userRole: UserRole?,
    viewModel: CompanyViewModel,
) {
    val isSystemAdmin = userRole == UserRole.SYSTEM_ADMIN
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri != null) viewModel.onImagePicked(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            CompanyLogo(
                logoUrl = uiState.pendingImageUri?.toString()
                    ?: uiState.company?.logoUrl.orEmpty(),
                companyName = uiState.company?.name.orEmpty(),
                isPending = uiState.pendingImageUri != null,
            )

            FilledIconButton(
                onClick = {
                    imagePicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = 4.dp, y = 4.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = SellviaPrimary,
                ),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Alterar logo",
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = uiState.company?.name.orEmpty(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Configurações da empresa",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        if (uiState.pendingImageUri != null) {
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = viewModel::onCancelImagePick,
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isUploadingImage,
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        val bytes = context.contentResolver
                            .openInputStream(uiState.pendingImageUri)
                            ?.use { it.readBytes() }
                        if (bytes != null) viewModel.onSaveImage(bytes)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isUploadingImage,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SellviaPrimary,
                    ),
                ) {
                    if (uiState.isUploadingImage) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Enviando…")
                    } else {
                        Icon(
                            Icons.Outlined.Upload,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Salvar logo")
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = LightSurface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CompanyTextField(
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    label = "Nome da empresa",
                    icon = Icons.Outlined.Business,
                    error = uiState.fieldErrors.name,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    ),
                )

                HorizontalDivider()

                CompanyTextField(
                    value = uiState.websiteUrl,
                    onValueChange = viewModel::onWebsiteUrlChange,
                    label = "Site da empresa",
                    icon = Icons.Outlined.Language,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Next,
                    ),
                )

                HorizontalDivider()

                CompanyTextField(
                    value = uiState.mainPhoneNumber,
                    onValueChange = viewModel::onPhoneChange,
                    label = "Telefone principal",
                    icon = Icons.Outlined.Phone,
                    error = uiState.fieldErrors.phone,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                    ),
                )

                if (isSystemAdmin) {
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = "Empresa ativa",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                text = "Desativar bloqueia o acesso de todos os usuários",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Switch(
                            checked = uiState.isActive,
                            onCheckedChange = viewModel::onIsActiveChange,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = viewModel::onSave,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving,
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Icon(Icons.Outlined.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Salvar alterações")
            }
        }
    }
}

@Composable
private fun CompanyLogo(
    logoUrl: String,
    companyName: String,
    isPending: Boolean,
) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(SellviaNotSelected),
        contentAlignment = Alignment.Center,
    ) {
        when {
            logoUrl.isNotBlank() -> AsyncImage(
                model = logoUrl,
                contentDescription = "Logo da empresa",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            else -> Text(
                text = companyName.take(1).uppercase(),
                color = SellviaPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
            )
        }

        if (isPending) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(SellviaPrimary.copy(alpha = 0.25f)),
            )
        }
    }
}

@Composable
private fun CompanyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    error: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp),
            )
        },
        singleLine = true,
        isError = error != null,
        supportingText = error?.let {
            { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall) }
        },
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}
