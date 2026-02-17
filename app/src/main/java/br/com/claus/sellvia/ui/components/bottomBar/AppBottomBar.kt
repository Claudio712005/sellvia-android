import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.claus.sellvia.core.navigation.NavigationItem
import br.com.claus.sellvia.ui.theme.LightBackground
import br.com.claus.sellvia.ui.theme.SellviaNotSelected
import br.com.claus.sellvia.ui.theme.SellviaPrimary
import br.com.claus.sellvia.ui.theme.SellviaPrimaryDark
import br.com.claus.sellvia.ui.theme.SellviaTertiary

@Composable
fun AppBottomBar(
    navController: NavHostController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar(
        containerColor = LightBackground.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            NavigationItem.entries.forEach { item ->

                val selected =
                    currentDestination?.hasRoute(item.route::class) == true

                val backgroundColor = if (selected) {
                    SellviaPrimary.copy(alpha = 0.15f)
                } else {
                    SellviaNotSelected.copy(alpha = 0.08f)
                }

                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(backgroundColor)
                        .clickable {
                            if (!selected) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (selected)
                            SellviaPrimary
                        else
                            SellviaPrimaryDark.copy(alpha = 0.6f),
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                    )
                }
            }
        }
    }
}

