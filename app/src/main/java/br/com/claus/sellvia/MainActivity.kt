package br.com.claus.sellvia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.claus.sellvia.core.navigation.AppNavHost
import br.com.claus.sellvia.core.theme.SellviaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SellviaTheme {
                AppNavHost()
            }
        }
    }
}