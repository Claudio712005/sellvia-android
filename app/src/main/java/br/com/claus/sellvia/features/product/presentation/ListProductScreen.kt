package br.com.claus.sellvia.features.product.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.features.product.presentation.components.ProductItem
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.ui.components.paginationTemplate.Paginator
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListProductScreen(
    modifier: Modifier = Modifier,
    bottomBarPadding: Dp = 0.dp,
    viewModel: ListProductsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()

    val emptyPagination = Pagination<Product>(emptyList(), 0, 0, 0, 10)
    val currentPagination = uiState.paginationData ?: emptyPagination

    if (uiState.error != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Erro: ${uiState.error}")
        }
        return
    }

    Paginator(
        modifier = modifier,
        bottomBarPadding = bottomBarPadding,
        paginationResponse = currentPagination,
        isLoading = uiState.isLoading,
        sortOptions = viewModel.sortOptions,
        selectedSort = selectedSort,
        onSortChange = { viewModel.updateSort(it) },
        onPageChange = { viewModel.fetchCategories(it) },
        onRefresh = { viewModel.fetchCategories(0) },
        itemContent = { ProductItem(it) },
        keySelector = { it.id }
    )
}