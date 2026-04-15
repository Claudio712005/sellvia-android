package br.com.claus.sellvia.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBusiness
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
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

    REGISTRY_PRODUCT(
        title = "Registrar Produto",
        icon = Icons.Outlined.AddShoppingCart,
        route = RegistryProductRoute,
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

    USERS(
        title = "Meu Perfil",
        icon = Icons.Outlined.People,
        route = UserRoute,
        visibleInBottomBar = false,
    ),

    COMPANY(
        title = "Empresa",
        icon = Icons.Outlined.Business,
        route = CompanyRoute,
        visibleInBottomBar = false,
    ),
}