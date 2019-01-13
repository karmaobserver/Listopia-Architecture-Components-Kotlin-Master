package com.aleksej.makaji.listopia.screen.shoppinglist

import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.usecase.GetShoppingListUseCase
import com.aleksej.makaji.listopia.data.usecase.GetShoppingListsUseCase
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListViewModel @Inject constructor(private val mGetShoppingListsUseCase: GetShoppingListsUseCase) : ViewModel() {

    val shoppingListsLiveData = mGetShoppingListsUseCase.invoke(Unit)
}