package com.aleksej.makaji.listopia.di

import android.app.Application
import com.aleksej.makaji.listopia.ListopiaApp
import com.aleksej.makaji.listopia.di.module.ActivityBindingModule
import com.aleksej.makaji.listopia.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Singleton
@Component(modules = [
    (AppModule::class),
    (ActivityBindingModule::class),
    (AndroidInjectionModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent

    }

    fun inject(listopiaApp: ListopiaApp)
}