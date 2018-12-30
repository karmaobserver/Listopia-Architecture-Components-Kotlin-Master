package com.aleksej.makaji.listopia.di.module

import com.aleksej.makaji.listopia.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun homeActivity(): HomeActivity
}