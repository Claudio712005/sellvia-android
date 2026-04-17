package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.features.catalog.data.CatalogRepositoryImpl
import br.com.claus.sellvia.features.catalog.data.CatalogService
import br.com.claus.sellvia.features.catalog.domain.repository.ICatalogRepository
import br.com.claus.sellvia.features.catalog.domain.usecase.DownloadCatalogUseCase
import br.com.claus.sellvia.features.catalog.presentation.CatalogViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val catalogModule = module {
    single { get<Retrofit>().create(CatalogService::class.java) }
    single<ICatalogRepository> { CatalogRepositoryImpl(service = get(), context = androidContext()) }
    factory { DownloadCatalogUseCase(get()) }
    viewModel { CatalogViewModel(get(), get()) }
}
