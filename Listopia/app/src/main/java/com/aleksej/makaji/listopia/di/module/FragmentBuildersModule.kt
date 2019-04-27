package com.aleksej.makaji.listopia.di.module

import com.aleksej.makaji.listopia.screen.productadd.ProductAddFragment
import com.aleksej.makaji.listopia.screen.productlist.ProductListFragment
import com.aleksej.makaji.listopia.screen.shoppinglist.ShoppingListFragment
import com.aleksej.makaji.listopia.screen.shoppinglistadd.ShoppingListAddFragment
import com.aleksej.makaji.listopia.screen.shoppinglistedit.ShoppingListEditFragment
import com.aleksej.makaji.listopia.screen.shoppinglistedit.ShoppingListEditViewModel
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeShoppingListFragment(): ShoppingListFragment

    @ContributesAndroidInjector
    abstract fun contributeShoppingListAddFragment(): ShoppingListAddFragment

    @ContributesAndroidInjector
    abstract fun contributeProductListFragment(): ProductListFragment

    @ContributesAndroidInjector
    abstract fun contributeProductAddFragment(): ProductAddFragment

    @ContributesAndroidInjector
    abstract fun contributeShoppingListEditFragment(): ShoppingListEditFragment
}