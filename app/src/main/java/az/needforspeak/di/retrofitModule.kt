package az.needforspeak.di

import android.content.Context
import android.content.SharedPreferences
import az.needforspeak.BuildConfig
import az.needforspeak.utils.AuthUtils
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val CONNECT_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 15L
private const val READ_TIMEOUT = 15L
val retrofitModule = module {
    single { Cache(androidApplication().cacheDir, 10L * 1024 * 1024) }
    single { GsonBuilder().create() }
    single { retrofitHttpClient() }
    single { retrofitBuilder() }
    factory { key ->
        androidContext().getSharedPreferences(key.get<String>(0), Context.MODE_PRIVATE)
    }


    single {
        Interceptor { chain ->
            chain.proceed(chain.request().newBuilder().apply {
                val sharedPreferences by inject<SharedPreferences> { parametersOf("secure") }
                val token = sharedPreferences.getString("token", "")
                val uAgent = sharedPreferences.getString("user_agent", "")
                val lang = sharedPreferences.getString("lang", "az")
                
                //if(!token.isNullOrEmpty()) {
                //    addHeader("Authorization", "Bear $token")
                //}else {
                //    sharedPreferences.edit().putString("token", "UU02939293").commit()
                //}
                if(!uAgent.isNullOrEmpty())
                    addHeader("User-Agent", uAgent)
                if(!lang.isNullOrEmpty()) {
                    addHeader("Accept-Language", lang)
                }
                addHeader("Authorization", AuthUtils.token)
                addHeader("Accept", "application/json")
                addHeader("Content-Type", "application/json")
            }.build())
        }
    }
}

private fun Scope.retrofitBuilder(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create(get()))
        //.addCallAdapterFactory(RxJava2CallAdapterFactory.create()) krn sudah pakai --> Coroutines
        .client(get())
        .build()
}


private fun Scope.retrofitHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().apply {
        cache(get())
        connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)
        addInterceptor(get<Interceptor>())
        addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.HEADERS
            }
            else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
//        addInterceptor(Interceptor { chain ->
//            val original = chain.request()
//            val newHeader = original.headers.newBuilder()
//                .add("Accept", "application/json")
//                .add("Content-Type", "application/json")
//                .add("User-Agent", "Mozilla").build()
//            val request = original.newBuilder()
//                .headers(newHeader)
//                .method(original.method, original.body)
//                .build()
//            return@Interceptor chain.proceed(request)
//
//        })

    }.build()
}