package br.com.claus.sellvia.core.navigation

import AppBottomBar
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.claus.sellvia.core.presentation.LoadingViewModel
import br.com.claus.sellvia.core.presentation.MainNavViewModel
import br.com.claus.sellvia.features.home.presentation.HomeScreen
import br.com.claus.sellvia.features.category.presentation.ListCategoryWithRegistry
import br.com.claus.sellvia.features.product.presentation.ListProductScreen
import br.com.claus.sellvia.features.product.presentation.RegistryProductScreen
import org.koin.androidx.compose.koinViewModel

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffoldNavGraph(
    startDestination: Any = HomeRoute
) {
    val loadingViewModel: LoadingViewModel = koinViewModel()
    val mainNavViewModel: MainNavViewModel = koinViewModel()

    val isLoading by loadingViewModel.isLoading.collectAsState()
    val fabVisible by mainNavViewModel.fabVisible.collectAsState()
    val fabAction by mainNavViewModel.fabAction.collectAsState()
    val openCategoryModal by mainNavViewModel.openCategoryModal.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentTitle = NavigationItem.entries.find { item ->
        navBackStackEntry?.destination?.hasRoute(item.route::class) == true
    }?.title ?: "Sellvia"

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = currentTitle,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        IconButton(onClick = { }, enabled = false) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Mais opções"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = fabVisible,
                    enter = fadeIn(tween(220)) + scaleIn(
                        initialScale = 0.8f,
                        animationSpec = spring(dampingRatio = 0.7f)
                    ),
                    exit = fadeOut(tween(150)) + scaleOut(targetScale = 0.8f)
                ) {
                    FloatingActionButton(onClick = { fabAction?.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar categoria"
                        )
                    }
                }
            },
            bottomBar = { AppBottomBar(navController) }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(top = padding.calculateTopPadding())
            ) {
                composable<HomeRoute> {
                    HomeScreen()
                }

                composable<CategoryRoute> {
                    DisposableEffect(Unit) {
                        mainNavViewModel.showFab { mainNavViewModel.openModal() }
                        onDispose { mainNavViewModel.hideFab() }
                    }
                    ListCategoryWithRegistry(
                        bottomBarPadding = padding.calculateBottomPadding(),
                        showModal = openCategoryModal,
                        openModal = { mainNavViewModel.openModal() },
                        onModalDismiss = { mainNavViewModel.closeModal() }
                    )
                }

                composable<ProductRoute> {
                    ListProductScreen(
                        bottomBarPadding = padding.calculateBottomPadding()
                    )
                }

                composable<RegistryProductRoute> {
                    RegistryProductScreen(
                        bottomBarPadding = padding.calculateBottomPadding(),
                        onNavigateToList = {
                            navController.navigate(ProductRoute) {
                                popUpTo<RegistryProductRoute> { inclusive = true }
                            }
                        }
                    )
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(enabled = false) { },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}