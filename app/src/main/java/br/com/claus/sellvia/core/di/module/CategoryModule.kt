package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.features.category.data.CategoryService
import br.com.claus.sellvia.features.category.data.CategoryRepositoryImpl
import br.com.claus.sellvia.features.category.domain.repository.ICategoryRepository
import br.com.claus.sellvia.features.category.domain.usecase.CreateCategoryUseCase
import br.com.claus.sellvia.features.category.domain.usecase.DeleteCategoryUseCase
import br.com.claus.sellvia.features.category.domain.usecase.GetCategoriesUseCase
import br.com.claus.sellvia.features.category.domain.usecase.UpdateCategoryUseCase
import br.com.claus.sellvia.features.category.presentation.ActionsCategoryViewModel
import br.com.claus.sellvia.features.category.presentation.ListCategoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val listCategoryModule = module {
    single { get<Retrofit>().create(CategoryService::class.java) }
    single<ICategoryRepository> { CategoryRepositoryImpl(get()) }

    factory { GetCategoriesUseCase(get()) }
    factory { CreateCategoryUseCase(get()) }
    factory { UpdateCategoryUseCase(get()) }
    factory { DeleteCategoryUseCase(get()) }

    viewModel { ListCategoryViewModel(get(), get()) }
}

val actionsCategoryModule = module {
    viewModel { ActionsCategoryViewModel(get(), get(), get(), get()) }
}