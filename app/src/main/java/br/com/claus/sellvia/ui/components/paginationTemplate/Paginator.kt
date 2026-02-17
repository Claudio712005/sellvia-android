package br.com.claus.sellvia.ui.components.paginationTemplate

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import br.com.claus.sellvia.ui.components.paginationTemplate.models.SortOption
import br.com.claus.sellvia.ui.theme.LightBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> Paginator(
    modifier: Modifier = Modifier,
    paginationResponse: Pagination<T>,
    isLoading: Boolean = false,
    keySelector: (T) -> Any,
    sortOptions: List<SortOption>,
    selectedSort: SortOption,
    onSortChange: (SortOption) -> Unit,
    onPageChange: (Int) -> Unit,
    onRefresh: () -> Unit = {},
    itemContent: @Composable (item: T) -> Unit,
    emptyStateContent: @Composable (() -> Unit)? = null,
    bottomBarPadding: Dp = 0.dp
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox (
        state = pullRefreshState,
        isRefreshing = isLoading,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize().background(LightBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            PaginationOptions(
                currentPage = paginationResponse.currentPage + 1,
                totalPages = paginationResponse.totalPages,
                totalItems = paginationResponse.totalItems,
                sortOptions = sortOptions,
                selectedSort = selectedSort,
                onPageChange = onPageChange,
                onSortChange = onSortChange,
                isLoading = isLoading
            )

            if (!isLoading && paginationResponse.items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(bottom = bottomBarPadding),
                    contentAlignment = Alignment.Center
                ) {
                    if (emptyStateContent != null) {
                        emptyStateContent()
                    } else {
                        DefaultEmptyState()
                    }
                }
            } else {
                AnimatedContent (
                    targetState = paginationResponse.items,
                    transitionSpec = {
                        fadeIn() + slideInVertically { it / 4 } togetherWith
                                fadeOut() + slideOutVertically { -it / 4 }
                    },
                    label = ""
                ) { animatedList ->

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = 16.dp + bottomBarPadding,
                            top = 8.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = animatedList,
                            key = keySelector
                        ) { item ->
                            itemContent(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultEmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Nenhum resultado encontrado",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}