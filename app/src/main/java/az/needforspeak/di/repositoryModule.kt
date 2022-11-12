package az.needforspeak.di

import az.needforspeak.repository.AuthRepositry
import az.needforspeak.repository.ChatsRepository
import az.needforspeak.repository.MainRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepositry(get()) }
    single { ChatsRepository(get()) }
    single { MainRepository(get()) }
}