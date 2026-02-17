package br.com.claus.sellvia.features.listCategory.presentation

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
import br.com.claus.sellvia.data.remote.model.response.CategoryResponse
import br.com.claus.sellvia.features.listCategory.presentation.components.CategoryItem
import br.com.claus.sellvia.ui.components.paginationTemplate.Paginator
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListCategoryScreen(
    modifier: Modifier = Modifier,
    bottomBarPadding: Dp = 0.dp,
    viewModel: ListCategoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()

    val emptyPagination = Pagination<CategoryResponse>(emptyList(), 0, 0, 0, 10)
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
        itemContent = { CategoryItem(it) },
        keySelector = { it.id ?: error("Category must have an ID") }
    )
}

