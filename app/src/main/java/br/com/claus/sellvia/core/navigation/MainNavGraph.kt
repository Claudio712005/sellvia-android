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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.claus.sellvia.features.home.presentation.HomeScreen
import br.com.claus.sellvia.features.listCategory.presentation.ListCategoryWithRegistry
import br.com.claus.sellvia.features.listCategory.presentation.components.ListCategoryFloatActionButton
import br.com.claus.sellvia.features.listProduct.presentation.ListProductScreen
import br.com.claus.sellvia.features.registryProduct.presentation.RegistryProductScreen

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffoldNavGraph(
    startDestination: Any = HomeRoute
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentTitle = NavigationItem.entries.find { item ->
        navBackStackEntry?.destination?.hasRoute(item.route::class) == true
    }?.title ?: "Sellvia"

    var openCategoryModal by remember { mutableStateOf(false) }

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
                    IconButton(onClick = {  }, enabled = false) {
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
        floatingActionButton = @Composable {
            if (navBackStackEntry?.destination?.hasRoute(CategoryRoute::class) == true) {
                ListCategoryFloatActionButton {
                    openCategoryModal = true
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
            composable<CategoryRoute>{
                ListCategoryWithRegistry(
                    bottomBarPadding = padding.calculateBottomPadding(),
                    showModal = openCategoryModal,
                    openModal = { openCategoryModal = true },
                    onModalDismiss = { openCategoryModal = false }
                )
            }
            composable<ProductRoute> {
                ListProductScreen(
                    bottomBarPadding = padding.calculateBottomPadding(),
                )
            }
            composable<RegistryProductRoute> {
                RegistryProductScreen(
                    bottomBarPadding = padding.calculateBottomPadding(),
                )
            }
        }
    }
}


