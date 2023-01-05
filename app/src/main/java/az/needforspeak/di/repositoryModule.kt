package az.needforspeak.di

import az.needforspeak.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepositry(get()) }
    single { ChatsRepository(get()) }
    single { MainRepository(get()) }
    single { AccountRepository(get()) }
    single { FriendsRepository(get()) }
}