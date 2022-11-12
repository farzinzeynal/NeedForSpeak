package az.needforspeak.di

import az.needforspeak.view_model.LoginViewModel
import az.needforspeak.view_model.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ChatsViewModel(get()) }
    viewModel { MessagingViewModel(get()) }
    viewModel { FriendsViewModel(get()) }
}
