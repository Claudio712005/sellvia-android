package br.com.claus.sellvia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.navigation.AppNavHost
import br.com.claus.sellvia.ui.theme.SellviaTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val tokenManager: TokenManager by inject ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SellviaTheme {
                AppNavHost(tokenManager)
            }
        }
    }
}