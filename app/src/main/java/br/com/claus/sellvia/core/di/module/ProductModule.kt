package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.data.remote.api.ProductService
import br.com.claus.sellvia.features.listProduct.data.ListProductRepository
import br.com.claus.sellvia.features.listProduct.presentation.ListProductsViewModel
import br.com.claus.sellvia.features.registryProduct.data.RegistryProductRepository
import br.com.claus.sellvia.features.registryProduct.presentation.RegistryProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val listProductModule = module {
    single { TokenManager(androidContext()) }
    single { get<Retrofit>().create(ProductService::class.java) }
    single { ListProductRepository(get()) }
    viewModel { ListProductsViewModel(get(), get()) }
}

val registryProductModule = module {
    single { TokenManager(androidContext()) }
    single {
        RegistryProductRepository(
            api = get<Retrofit>().create(ProductService::class.java),
            context = androidContext()
        )
    }
    viewModel { RegistryProductViewModel(get(), get(), get()) }
}