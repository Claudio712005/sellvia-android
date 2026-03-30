package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.data.remote.api.CategoryService
import br.com.claus.sellvia.features.listCategory.data.ListCategoryRepository
import br.com.claus.sellvia.features.listCategory.presentation.ListCategoryViewModel
import br.com.claus.sellvia.features.listCategory.presentation.ActionsCategoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val listCategoryModule = module {
    single { TokenManager(androidContext()) }
    single { get<Retrofit>().create(CategoryService::class.java) }
    single { ListCategoryRepository(get()) }
    viewModel { ListCategoryViewModel(get(), get()) }
}

val actionsCategoryModule = module{
    single { TokenManager(androidContext()) }
    single { get<Retrofit>().create(CategoryService::class.java) }
    single { ListCategoryRepository(get()) }
    viewModel { ActionsCategoryViewModel(get(), get()) }
}