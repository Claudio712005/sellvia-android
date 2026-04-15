package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.features.company.data.CompanyRepositoryImpl
import br.com.claus.sellvia.features.company.data.CompanyService
import br.com.claus.sellvia.features.company.domain.repository.ICompanyRepository
import br.com.claus.sellvia.features.company.domain.usecase.GetCompanyUseCase
import br.com.claus.sellvia.features.company.domain.usecase.UpdateCompanyImageUseCase
import br.com.claus.sellvia.features.company.domain.usecase.UpdateCompanyUseCase
import br.com.claus.sellvia.features.company.presentation.CompanyViewModel
import br.com.claus.sellvia.features.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val companyModule = module {
    single { get<Retrofit>().create(CompanyService::class.java) }
    single<ICompanyRepository> { CompanyRepositoryImpl(service = get()) }
    factory { GetCompanyUseCase(get()) }
    factory { UpdateCompanyUseCase(get()) }
    factory { UpdateCompanyImageUseCase(get()) }
    viewModel { CompanyViewModel(get(), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
}
