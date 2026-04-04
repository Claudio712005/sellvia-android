package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.core.presentation.LoadingViewModel
import org.koin.dsl.module

val coreModule = module {
    single { LoadingViewModel() }
}