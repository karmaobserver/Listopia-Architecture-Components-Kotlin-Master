package com.aleksej.makaji.listopia.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksej.makaji.listopia.di.annotation.ViewModelKey
import com.aleksej.makaji.listopia.factory.ViewModelFactory
import com.aleksej.makaji.listopia.screen.productadd.ProductAddViewModel
import com.aleksej.makaji.listopia.screen.productlist.ProductListViewModel
import com.aleksej.makaji.listopia.screen.shoppinglist.ShoppingListViewModel
import com.aleksej.makaji.listopia.screen.shoppinglistadd.ShoppingListAddViewModel
import com.aleksej.makaji.listopia.screen.shoppinglistedit.ShoppingListEditViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ShoppingListViewModel::class)
    abstract fun bindShoppingListViewModel(shoppingListViewModel: ShoppingListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShoppingListAddViewModel::class)
    abstract fun bindShoppingListAddViewModel(shoppingListAddViewModel: ShoppingListAddViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductListViewModel::class)
    abstract fun bindProductListViewModel(productListViewModel: ProductListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductAddViewModel::class)
    abstract fun bindProductAddViewModel(productAddViewModel: ProductAddViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShoppingListEditViewModel::class)
    abstract fun bindShoppingListEditViewModel(shoppingListEditViewModel: ShoppingListEditViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}