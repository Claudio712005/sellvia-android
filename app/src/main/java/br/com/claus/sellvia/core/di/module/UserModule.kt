package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.features.user.data.UserRepositoryImpl
import br.com.claus.sellvia.features.user.data.UserService
import br.com.claus.sellvia.features.user.domain.repository.IUserRepository
import br.com.claus.sellvia.features.user.domain.usecase.GetUserProfileUseCase
import br.com.claus.sellvia.features.user.presentation.UserProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val userModule = module {
    single { get<Retrofit>().create(UserService::class.java) }
    single<IUserRepository> { UserRepositoryImpl(service = get()) }
    factory { GetUserProfileUseCase(get()) }
    viewModel { UserProfileViewModel(get(), get(), get()) }
}
