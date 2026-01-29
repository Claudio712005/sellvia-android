package br.com.claus.sellvia.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthRoute
    ) {

        composable<AuthRoute> {
            AuthNavGraph(
                onAuthSuccess = {
                    navController.navigate(MainRoute) {
                        popUpTo(AuthRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<MainRoute> {
            MainScaffoldNavGraph()
        }
    }
}
