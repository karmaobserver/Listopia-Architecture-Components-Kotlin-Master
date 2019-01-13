package com.aleksej.makaji.listopia.di.module

import com.aleksej.makaji.listopia.screen.shoppinglist.ShoppingListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeShoppingListFragment(): ShoppingListFragment
}