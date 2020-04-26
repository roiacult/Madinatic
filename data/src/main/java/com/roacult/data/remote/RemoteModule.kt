package com.roacult.data.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.roacult.data.BuildConfig
import com.roacult.data.remote.services.AuthService
import com.roacult.data.utils.BASE_URL
import com.roacult.data.utils.createService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val remoteModule = module {

    //provide json parser Gson
    single { Gson() }

    //provide HttpClient
    single {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                addNetworkInterceptor(StethoInterceptor())
            }
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
        }.build()
    }

    //provide retrofit
    single {
        Retrofit.Builder().apply {
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addConverterFactory(GsonConverterFactory.create())
            addConverterFactory(ScalarsConverterFactory.create())
            client(get())
            baseUrl(BASE_URL)
        }.build()
    }

    single{ createService(AuthService::class.java) }

    single { AuthRemote(get()) }
}