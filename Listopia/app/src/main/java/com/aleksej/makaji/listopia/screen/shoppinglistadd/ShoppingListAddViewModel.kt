package com.aleksej.makaji.listopia.screen.shoppinglistadd

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.usecase.SaveShoppingListUseCase
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/13/19.
 */
class ShoppingListAddViewModel @Inject constructor(private val mSaveShoppingListUseCase: SaveShoppingListUseCase) : ViewModel() {

    private val _saveShoppingList = MutableLiveData<ShoppingListValue>()
    val saveShoppingListLiveData = Transformations.switchMap(_saveShoppingList) {
        mSaveShoppingListUseCase.invoke(it)
    }

    fun createShoppingList(shoppingListValue: ShoppingListValue) {
        _saveShoppingList.postValue(shoppingListValue)
    }
}