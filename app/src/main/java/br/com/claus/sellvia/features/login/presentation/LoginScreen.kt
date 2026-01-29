package br.com.claus.sellvia.features.login.presentation

import PoweredByClaus
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.R
import br.com.claus.sellvia.core.theme.SellviaBackground
import br.com.claus.sellvia.features.login.presentation.components.AnimatedCurvedBackground
import br.com.claus.sellvia.features.login.presentation.components.BackToLoginButton
import br.com.claus.sellvia.features.login.presentation.components.LoginForm
import br.com.claus.sellvia.ui.components.buttons.SellviaPrimaryButton
import br.com.claus.sellvia.ui.components.buttons.SellviaSecondaryButton

@Composable
fun LoginScreenContent(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = org.koin.androidx.compose.koinViewModel()
) {

    val state by viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.onEvent(LoginEvent.Start)
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SellviaBackground)
    ) {

        AnimatedCurvedBackground(state.showLoginForm)

        BackToLoginButton(
            visible = state.showLoginForm,
            onClick = { viewModel.onEvent(LoginEvent.ClickBack) },
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(56.dp))

            AnimatedVisibility(
                visible = state.startAnimation,
                enter = fadeIn(tween(800)) +
                        slideInVertically(
                            initialOffsetY = { -40 },
                            animationSpec = tween(800)
                        )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_sellvia_removebg),
                    contentDescription = "Sellvia Logo",
                    modifier = Modifier.height(284.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = state.startAnimation && !state.showLoginForm,
                enter = fadeIn(tween(700, delayMillis = 300)) +
                        slideInVertically(
                            initialOffsetY = { 40 },
                            animationSpec = tween(700, delayMillis = 300)
                        ),
                exit = fadeOut(tween(300))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    SellviaPrimaryButton(
                        text = "Entrar",
                        onClick = {
                            viewModel.onEvent(LoginEvent.ClickEnter)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SellviaSecondaryButton(
                        text = "Criar conta"
                    )
                }
            }

            AnimatedVisibility(
                visible = state.showLoginForm,
                enter = fadeIn(tween(500)) +
                        slideInVertically(
                            initialOffsetY = { 60 },
                            animationSpec = tween(500, easing = FastOutSlowInEasing)
                        ) +
                        expandVertically(
                            animationSpec = tween(500, easing = FastOutSlowInEasing)
                        )
            ) {
                LoginForm(
                    email = state.email,
                    password = state.password,
                    isLoading = state.isLoading,
                    error = state.error,
                    onEmailChange = {
                        viewModel.onEvent(LoginEvent.EmailChanged(it))
                    },
                    onPasswordChange = {
                        viewModel.onEvent(LoginEvent.PasswordChanged(it))
                    },
                    onSubmit = {
                        viewModel.onEvent(LoginEvent.SubmitLogin)
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = state.startAnimation,
                enter = fadeIn(tween(600, delayMillis = 600))
            ) {
                PoweredByClaus()
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp"
)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent(
            onLoginSuccess = {}
        )
    }
}
