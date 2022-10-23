package az.needforspeak.di

import androidx.room.Room
import az.needforspeak.db.AppDatabase
import az.needforspeak.db.ChatDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
//    single {
//        Room.databaseBuilder(
//            androidApplication(),
//            AppDatabase::class.java,
//            "AppDatabase"
//        ).build()
//    }
//    single {
//        get<AppDatabase>().chatDao()
//    }

}