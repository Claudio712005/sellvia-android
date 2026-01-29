package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.features.login.data.remote.LoginApi
import br.com.claus.sellvia.features.login.data.remote.mock.MockLoginApi
import br.com.claus.sellvia.features.login.data.repository.LoginRepositoryImpl
import br.com.claus.sellvia.features.login.domain.repository.LoginRepository
import br.com.claus.sellvia.features.login.domain.usecase.LoginUseCase
import br.com.claus.sellvia.features.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {

    single<LoginApi> { MockLoginApi() }

    single<LoginRepository> {
        LoginRepositoryImpl(get())
    }

    single {
        LoginUseCase(get())
    }

    viewModel {
        LoginViewModel(get())
    }
}
