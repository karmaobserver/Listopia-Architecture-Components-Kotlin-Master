package com.aleksej.makaji.listopia.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideResources(application: Application): Resources {
        return application.resources
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("com.aleksej.makaji.listopia.android.shop.prefs", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences): SharedPreferenceManager {
        return SharedPreferenceManager(sharedPreferences)
    }
}