import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.R
import br.com.claus.sellvia.ui.theme.SellviaTertiary

@Composable
fun PoweredByClaus() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Powered by",
            style = MaterialTheme.typography.bodySmall,
            color = SellviaTertiary.copy(alpha = 0.6f)
        )

        Image(
            painter = painterResource(id = R.drawable.claus_logo),
            contentDescription = "Claus Logo",
            modifier = Modifier.height(44.dp).padding(0.dp, 4.dp, 0.dp, 0.dp)
        )
    }
}
