package br.com.claus.sellvia.features.login.presentation

import PoweredByClaus
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.R
import br.com.claus.sellvia.features.login.presentation.components.LoginForm
import br.com.claus.sellvia.ui.components.buttons.SellviaPrimaryButton
import br.com.claus.sellvia.ui.theme.SellviaPrimary
import br.com.claus.sellvia.ui.theme.SellviaTheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.Image

@Composable
fun LoginScreenContent(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_sellvia_removebg),
                contentDescription = "Sellvia Logo",
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Bem-vindo ao Sellvia",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Gerencie seus produtos de forma inteligente e rápida.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoginForm(
                username = viewModel.username,
                onEmailChange = { viewModel.username = it },
                password = viewModel.password,
                onPasswordChange = { viewModel.password = it },
                rememberMe = viewModel.rememberMe,
                onRememberMeChange = { viewModel.rememberMe = it },
            )

            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SellviaPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = if (viewModel.isLoading) "Aguarde..." else "Entrar",
                onClick = { viewModel.onLoginClick(onLoginSuccess) },
                enabled = !viewModel.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
                Text(
                    text = " Ou entrar com ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { }, enabled = false) {
                Text(
                    text = buildAnnotatedString {
                        append("Ainda não tem conta? ")
                        withStyle(style = SpanStyle(color = SellviaPrimary, fontWeight = FontWeight.Bold)) {
                            append("Cadastre-se")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            PoweredByClaus()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, device = "spec:width=390dp,height=844dp")
@Composable
fun LoginScreenPreview() {
    SellviaTheme {
        LoginScreenContent(onLoginSuccess = {})
    }
}