package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.features.product.data.ProductService
import br.com.claus.sellvia.features.product.presentation.ListProductsViewModel
import br.com.claus.sellvia.features.product.data.ProductRepositoryImpl
import br.com.claus.sellvia.features.product.domain.repository.IProductRepository
import br.com.claus.sellvia.features.product.domain.usecase.CreateProductUseCase
import br.com.claus.sellvia.features.product.domain.usecase.GetProductsUseCase
import br.com.claus.sellvia.features.product.presentation.RegistryProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val listProductModule = module {
    single { get<Retrofit>().create(ProductService::class.java) }
    single<IProductRepository> { ProductRepositoryImpl(service = get(), context = androidContext()) }

    factory { GetProductsUseCase(get()) }
    factory { CreateProductUseCase(get()) }

    viewModel { ListProductsViewModel(get(), get()) }
}

val registryProductModule = module {
    viewModel { RegistryProductViewModel(androidContext(), get(), get(), get()) }
}