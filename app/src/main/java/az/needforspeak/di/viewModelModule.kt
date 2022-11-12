package az.needforspeak.di

import az.needforspeak.view_model.MainViewModel
import az.needforspeak.view_model.SplashViewModel
import az.needforspeak.view_model.ChatsViewModel
import az.needforspeak.view_model.MessagingViewModel
import az.needforspeak.ui.unregister.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ChatsViewModel(get()) }
    viewModel { MessagingViewModel(get()) }
}
