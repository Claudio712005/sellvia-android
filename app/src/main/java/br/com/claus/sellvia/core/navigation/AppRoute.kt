package br.com.claus.sellvia.core.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute

@Serializable
sealed interface RootRoute : AppRoute

@Serializable
object AuthRoute : RootRoute

@Serializable
object MainRoute : RootRoute

@Serializable
sealed interface AuthNavRoute : AppRoute

@Serializable
data class SplashRoute(
    val companyLogoUrl: String? = null
) : AuthNavRoute

@Serializable
object LoginRoute : AuthNavRoute

@Serializable
sealed interface MainNavRoute : AppRoute

@Serializable
object HomeRoute : MainNavRoute

@Serializable
object ProductRoute: MainNavRoute
