package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.data.remote.api.LoginService
import br.com.claus.sellvia.features.login.data.LoginRepository
import br.com.claus.sellvia.features.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val loginmodule = module {
    single {get<Retrofit>().create(LoginService::class.java)}
    single { LoginRepository(get()) }
    viewModel { LoginViewModel(get()) }
}