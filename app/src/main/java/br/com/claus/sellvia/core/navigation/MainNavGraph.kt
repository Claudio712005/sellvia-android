package br.com.claus.sellvia.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.claus.sellvia.features.home.presentation.HomeScreen
import br.com.claus.sellvia.ui.components.bottomBar.AppBottomBar

@Composable
fun MainScaffoldNavGraph() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(padding)
        ) {

            composable<HomeRoute> {
                HomeScreen()
            }

        }
    }
}


