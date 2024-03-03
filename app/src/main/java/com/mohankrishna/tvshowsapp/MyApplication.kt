package com.mohankrishna.tvshowsapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.mohankrishna.tvshowsapp.Repository.RoomRepository.MyRoomDatabase
import com.mohankrishna.tvshowsapp.utils.InternetModeProvider
import com.mohankrishna.tvshowsapp.viewModels.DetailsScreenViewModel
import com.mohankrishna.tvshowsapp.viewModels.HomeScreenViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.mohankrishna.tvshowsapp.Repository.RetrofitRepository.TvShowsApiInterface as TvShowsApiInterface

class MyApplication:Application() {
    companion object {
        private var instance: MyApplication? = null
        lateinit var mydatabase: MyRoomDatabase
        fun getContext(): Context? {
            return instance
        }
    }
    override fun onCreate() {
        super.onCreate()

        buildInstanceForRoom()

        instance = this
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(retrofitModules,viewModelModules,appUtils))
        }
    }

    private fun buildInstanceForRoom() {
        mydatabase = Room.databaseBuilder(
            applicationContext,
            MyRoomDatabase::class.java,
            "new_databse"
        ).build()
    }

    /* val roomDatabase= module {
         single { DatabaseClient.initialize(get())  }
         single<RoomDatabaseHelper> { DatabaseHelperImpl(get()) }
     }*/
    val retrofitModules = module {
        single {
            var base_url=BuildConfig.BASE_URL
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            var client=OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
            Retrofit.Builder()
                .baseUrl(base_url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .build()
                .create(TvShowsApiInterface::class.java)
        }
    }
    val viewModelModules= module{
        single { HomeScreenViewModel(get())}
        single { DetailsScreenViewModel(get()) }
    }

    val appUtils= module {
        single { InternetModeProvider(get()) }
    }

}