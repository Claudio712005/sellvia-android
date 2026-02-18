package br.com.claus.sellvia.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBusiness
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: MainNavRoute,
    val visibleInBottomBar: Boolean = true
) {

    HOME(
        title = "Home",
        icon = Icons.Outlined.Home,
        route = HomeRoute
    ),

    PRODUCTS(
        title = "Produtos",
        icon = Icons.Outlined.AddBusiness,
        route = ProductRoute
    ),

    CATEGORIES(
        title = "Categorias",
        icon = Icons.Outlined.BookmarkAdd,
        route = CategoryRoute
    ),
}