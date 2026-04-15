package br.com.claus.sellvia.core.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.claus.sellvia.core.data.storage.SessionGuard
import br.com.claus.sellvia.core.data.storage.TokenManager
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun AppNavHost(tokenManager: TokenManager) {
    val navController = rememberNavController()

    val tokenState by tokenManager.accessToken.collectAsState(initial = "loading")

    if (tokenState == "loading") return

    LaunchedEffect(tokenState) {
        if (SessionGuard.sessionActive) return@LaunchedEffect

        val keepLoggedIn = tokenManager.keepLoggedIn().firstOrNull() ?: true
        if (!keepLoggedIn && tokenState != null) {
            tokenManager.clearTokens()
        }

        SessionGuard.sessionActive = true
    }

    val startDest = if (tokenState != null) "main_graph" else "auth_graph"

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable("auth_graph") {
            AuthNavGraph(
                onAuthSuccess = {
                    navController.navigate("main_graph") {
                        popUpTo("auth_graph") { inclusive = true }
                    }
                }
            )
        }

        composable("main_graph") {
            MainScaffoldNavGraph()
        }
    }

    LaunchedEffect(tokenState) {
        if (tokenState == null) {
            navController.navigate("auth_graph") {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
