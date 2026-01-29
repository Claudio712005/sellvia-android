package br.com.claus.sellvia.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: MainNavRoute
) {

    HOME(
        title = "Home",
        icon = Icons.Outlined.Home,
        route = HomeRoute
    )
}