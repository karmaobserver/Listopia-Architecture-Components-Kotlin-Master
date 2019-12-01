package com.aleksej.makaji.listopia.di.module

import com.aleksej.makaji.listopia.service.ListopiaFirebaseService
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Aleksej Makaji on 2019-12-01.
 */
@Module
internal abstract class ServiceModule {

    @ContributesAndroidInjector
    internal abstract fun contributePortierFirebaseService(): ListopiaFirebaseService
}