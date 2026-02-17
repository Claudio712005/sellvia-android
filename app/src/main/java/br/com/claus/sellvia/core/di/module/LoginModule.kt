package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.data.remote.api.LoginService
import br.com.claus.sellvia.features.login.data.LoginRepository
import br.com.claus.sellvia.features.login.presentation.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val loginModule = module {

    single { TokenManager(androidContext()) }

    single<LoginService>(named("mainService")) {
        get<Retrofit>().create(LoginService::class.java)
    }

    single {
        LoginRepository(
            api = get(named("mainService"))
        )
    }

    viewModel {
        LoginViewModel(
            repository = get(),
            tokenManager = get()
        )
    }
}
