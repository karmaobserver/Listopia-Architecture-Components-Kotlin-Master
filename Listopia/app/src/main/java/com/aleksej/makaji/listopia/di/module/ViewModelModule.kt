package com.aleksej.makaji.listopia.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksej.makaji.listopia.di.annotation.ViewModelKey
import com.aleksej.makaji.listopia.factory.ViewModelFactory
import com.aleksej.makaji.listopia.viewmodel.ProductViewModel
import com.aleksej.makaji.listopia.viewmodel.ShoppingListViewModel
import com.aleksej.makaji.listopia.viewmodel.UserViewModel
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
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindProductViewModel(productViewModel: ProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}