package br.com.claus.sellvia.features.home.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.claus.sellvia.features.catalog.presentation.CatalogViewModel
import br.com.claus.sellvia.features.catalog.presentation.components.CatalogBottomSheet
import br.com.claus.sellvia.features.home.presentation.components.HomeBlock
import br.com.claus.sellvia.features.home.presentation.components.HomeBlockData
import br.com.claus.sellvia.ui.theme.SellviaNotSelected
import br.com.claus.sellvia.ui.theme.SellviaPrimary
import br.com.claus.sellvia.ui.theme.SellviaPrimaryDark
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCompany: (() -> Unit)? = null,
    catalogViewModel: CatalogViewModel = koinViewModel(),
    homeViewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by catalogViewModel.uiState.collectAsState()
    val homeState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.downloadMessage) {
        val msg = uiState.downloadMessage ?: return@LaunchedEffect
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        catalogViewModel.dismissDownloadMessage()
    }

    val blocks = buildList {
        add(
            HomeBlockData(
                title = "Catálogo de Produtos",
                subtitle = "Gere e baixe o catálogo completo em PDF",
                icon = Icons.Outlined.PictureAsPdf,
                badge = "PDF",
                onClick = { catalogViewModel.onOpenSheet() },
            )
        )
        if (onNavigateToCompany != null) {
            add(
                HomeBlockData(
                    title = "Configurações da Empresa",
                    subtitle = "Edite nome, site, telefone e logo",
                    icon = Icons.Outlined.Business,
                    onClick = onNavigateToCompany,
                )
            )
        }
        add(
            HomeBlockData(
                title = "Configurações da Conta",
                subtitle = "Gerencie seu perfil e altere sua senha",
                icon = Icons.Outlined.ManageAccounts,
                onClick = onNavigateToProfile,
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        CompanyHeroHeader(
            companyName = homeState.companyName,
            logoUrl = homeState.companyLogoUrl,
            isLoading = homeState.isLoading,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "O que deseja fazer?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(4.dp))

            blocks.forEach { block ->
                HomeBlock(data = block)
            }
        }
    }

    if (uiState.showSheet) {
        CatalogBottomSheet(
            uiState = uiState,
            onDismiss = { catalogViewModel.onDismissSheet() },
            onDisplayOptionsUpdate = { catalogViewModel.onDisplayOptionsUpdate(it) },
            onFilterOptionsUpdate = { catalogViewModel.onFilterOptionsUpdate(it) },
            onDownload = { catalogViewModel.onDownload() },
        )
    }
}

@Composable
private fun CompanyHeroHeader(
    companyName: String,
    logoUrl: String,
    isLoading: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SellviaPrimaryDark, SellviaPrimary)
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(SellviaNotSelected),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    isLoading -> CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = SellviaPrimary,
                        strokeWidth = 2.dp,
                    )
                    logoUrl.isNotBlank() -> AsyncImage(
                        model = logoUrl,
                        contentDescription = "Logo da empresa",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    companyName.isNotBlank() -> Text(
                        text = companyName.take(1).uppercase(),
                        color = SellviaPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                    )
                }
            }

            Column {
                Text(
                    text = "Bem-vindo!",
                    style = MaterialTheme.typography.labelMedium,
                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.75f),
                )
                if (companyName.isNotBlank()) {
                    Text(
                        text = companyName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.White,
                    )
                }
            }
        }
    }
}
