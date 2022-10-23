package az.needforspeak.di

import az.needforspeak.ui.MainViewModel
import az.needforspeak.ui.SplashViewModel
import az.needforspeak.ui.register.ChatsViewModel
import az.needforspeak.ui.register.MessagingViewModel
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
