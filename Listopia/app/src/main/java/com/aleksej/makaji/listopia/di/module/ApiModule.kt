package com.aleksej.makaji.listopia.di.module

import android.app.Application
import com.aleksej.makaji.listopia.BuildConfig
import com.aleksej.makaji.listopia.data.api.ListopiaApi
import com.aleksej.makaji.listopia.data.api.ListopiaApiInterceptor
import com.aleksej.makaji.listopia.di.annotation.Listopia
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun provideHttpCatche(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideStethoInterceptorCatche(): StethoInterceptor {
        return StethoInterceptor()
    }

    @Provides
    @Singleton
    fun provideListopiaApiInterceptor(sharedPreferenceManager: SharedPreferenceManager): ListopiaApiInterceptor {
        return ListopiaApiInterceptor(sharedPreferenceManager)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    @Listopia
    fun providesApiOkHttpBuilder(cache: Cache, stethoInterceptor: StethoInterceptor, listopiaApiInterceptor: ListopiaApiInterceptor): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.cache(cache)
        clientBuilder.addInterceptor(listopiaApiInterceptor)
        if (BuildConfig.DEBUG) {
            clientBuilder.addNetworkInterceptor(stethoInterceptor)
        }
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        clientBuilder.readTimeout(30, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(30, TimeUnit.SECONDS)

        return clientBuilder.build()
    }

    @Singleton
    @Provides
    @Listopia
    fun provideRetrofit(gson: Gson, @Listopia okHttpClient: OkHttpClient): Retrofit {
        return getRetrofit(BuildConfig.BASE_URL, gson, okHttpClient)
    }

    @Provides
    @Singleton
    fun provideApiServices(@Listopia retrofit: Retrofit): ListopiaApi {
        return retrofit.create<ListopiaApi>(ListopiaApi::class.java)
    }

    private fun getRetrofit(baseUrl: String, gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
    }
}