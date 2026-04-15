package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.features.catalog.data.CatalogDownloadManager
import br.com.claus.sellvia.features.catalog.data.CatalogRepositoryImpl
import br.com.claus.sellvia.features.catalog.domain.repository.ICatalogRepository
import br.com.claus.sellvia.features.catalog.domain.usecase.DownloadCatalogUseCase
import br.com.claus.sellvia.features.catalog.presentation.CatalogViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val catalogModule = module {
    single { CatalogDownloadManager(context = androidContext()) }
    single<ICatalogRepository> { CatalogRepositoryImpl(downloadManager = get()) }
    factory { DownloadCatalogUseCase(get()) }
    viewModel { CatalogViewModel(get(), get()) }
}
