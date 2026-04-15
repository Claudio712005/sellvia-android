package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.presentation.LoadingViewModel
import br.com.claus.sellvia.core.presentation.MainNavViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    single { TokenManager(androidContext()) }
    single { LoadingViewModel() }
    viewModel { MainNavViewModel(get()) }
}