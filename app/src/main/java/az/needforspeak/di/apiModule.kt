package az.needforspeak.di

import az.needforspeak.data.AccountService
import az.needforspeak.data.AuthService
import az.needforspeak.data.FriendsService
import az.needforspeak.data.MainService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single(createdAtStart = false) { get<Retrofit>().create(AuthService::class.java) }
    single(createdAtStart = false) { get<Retrofit>().create(MainService::class.java) }
    single(createdAtStart = false) { get<Retrofit>().create(AccountService::class.java) }
    single(createdAtStart = false) { get<Retrofit>().create(FriendsService::class.java) }
}