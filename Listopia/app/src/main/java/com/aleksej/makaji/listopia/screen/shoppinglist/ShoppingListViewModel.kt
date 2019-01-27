package com.aleksej.makaji.listopia.screen.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.usecase.GetShoppingListsUseCase
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListViewModel @Inject constructor(private val mGetShoppingListsUseCase: GetShoppingListsUseCase) : ViewModel() {

    val shoppingListsLiveData = mGetShoppingListsUseCase.invoke(Unit)

    private val _addShoppingListEvent = MutableLiveData<StateHandler<Unit>>()
    val addShoppingListEvent : LiveData<StateHandler<Unit>>
        get() = _addShoppingListEvent

    fun addShoppingListEvent() {
        _addShoppingListEvent.postValue(StateHandler.success(Unit))
    }
}