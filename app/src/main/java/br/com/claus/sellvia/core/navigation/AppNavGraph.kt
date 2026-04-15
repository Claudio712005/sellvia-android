package br.com.claus.sellvia.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.com.claus.sellvia.features.login.presentation.LoginScreenContent
import br.com.claus.sellvia.features.splash.presentation.SplashScreen

@Composable
fun AuthNavGraph(
    onAuthSuccess: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashRoute()
    ) {

        composable<SplashRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<SplashRoute>()

            SplashScreen(
                companyLogoUrl = route.companyLogoUrl,
                onFinished = {
                    if (route.companyLogoUrl == null) {
                        navController.navigate(LoginRoute)
                    } else {
                        onAuthSuccess()
                    }
                }
            )
        }

        composable<LoginRoute> {
            LoginScreenContent(
                onLoginSuccess = {
                    navController.navigate(
                        SplashRoute(
                            companyLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/a/ab/Logo_TV_2015.png"
                        )
                    ) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                }
            )
        }
    }
}
