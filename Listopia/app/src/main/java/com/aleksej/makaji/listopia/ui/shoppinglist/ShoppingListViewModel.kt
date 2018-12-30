package com.aleksej.makaji.listopia.ui.shoppinglist

import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.usecase.GetShoppingListsUseCase
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListViewModel @Inject constructor(private val mGetShoppingListsUseCase: GetShoppingListsUseCase) : ViewModel() {

    val test = "Test ViewModel"

    val shoppingListsEvent = mGetShoppingListsUseCase.invoke(Unit)

}