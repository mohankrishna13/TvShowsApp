package com.mohankrishna.tvshowsapp

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import com.mohankrishna.tvshowsapp.Repository.RoomRepository.DatabaseClient
import com.mohankrishna.tvshowsapp.Repository.RoomRepository.RoomDatabaseHelper
import com.mohankrishna.tvshowsapp.Repository.RoomRepository.DatabaseHelperImpl
import com.mohankrishna.tvshowsapp.Repository.RoomRepository.TvServiceRoomDao
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
        fun getContext(): Context? {
            return instance
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(retrofitModules,viewModelModules,appUtils,roomDatabase))
        }
    }

    val roomDatabase= module {
        single { DatabaseClient.initialize(this@MyApplication)  }
        single<RoomDatabaseHelper> { DatabaseHelperImpl(get()) }
    }
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
        single { HomeScreenViewModel(get(),get())}
        single { DetailsScreenViewModel(get(),get()) }
    }

    val appUtils= module {
        single { InternetModeProvider(get()) }
    }

}