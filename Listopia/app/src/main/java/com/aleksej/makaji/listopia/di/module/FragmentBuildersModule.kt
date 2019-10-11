package com.aleksej.makaji.listopia.di.module

import com.aleksej.makaji.listopia.view.friend.FriendAddFragment
import com.aleksej.makaji.listopia.view.friend.FriendListFragment
import com.aleksej.makaji.listopia.view.product.ProductAddFragment
import com.aleksej.makaji.listopia.view.product.ProductEditFragment
import com.aleksej.makaji.listopia.view.product.ProductListFragment
import com.aleksej.makaji.listopia.view.shoppinglist.ShoppingListFragment
import com.aleksej.makaji.listopia.view.shoppinglist.ShoppingListAddFragment
import com.aleksej.makaji.listopia.view.shoppinglist.ShoppingListEditFragment
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
    abstract fun contributeProductEditFragment(): ProductEditFragment

    @ContributesAndroidInjector
    abstract fun contributeShoppingListEditFragment(): ShoppingListEditFragment

    @ContributesAndroidInjector
    abstract fun contributeFriendListFragment(): FriendListFragment

    @ContributesAndroidInjector
    abstract fun contributeFriendAddFragment(): FriendAddFragment
}