package com.aleksej.makaji.listopia.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.work.WorkerFactory
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.factory.DaggerWorkerFactory
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Module(includes = [ApiModule::class, RepositoryModule::class, ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideResources(application: Application): Resources {
        return application.resources
    }

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("com.aleksej.makaji.listopia.prefs", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences): SharedPreferenceManager {
        return SharedPreferenceManager(sharedPreferences)
    }
}