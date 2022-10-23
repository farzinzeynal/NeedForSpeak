package az.needforspeak.di

import az.needforspeak.data.AuthService
import az.needforspeak.data.MainService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single(createdAtStart = false) { get<Retrofit>().create(AuthService::class.java) }
    single(createdAtStart = false) { get<Retrofit>().create(MainService::class.java) }
}