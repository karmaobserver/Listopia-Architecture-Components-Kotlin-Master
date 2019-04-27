package com.aleksej.makaji.listopia.screen.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.usecase.DeleteShoppingListByIdUseCase
import com.aleksej.makaji.listopia.data.usecase.GetShoppingListsUseCase
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListViewModel @Inject constructor(private val mGetShoppingListsUseCase: GetShoppingListsUseCase,
                                                private val mDeleteShoppingListByIdUseCase: DeleteShoppingListByIdUseCase) : ViewModel() {

    val shoppingListsLiveData = mGetShoppingListsUseCase.invoke(Unit)

    private val _addShoppingListEvent = MutableLiveData<StateHandler<Unit>>()
    val addShoppingListEvent : LiveData<StateHandler<Unit>>
        get() = _addShoppingListEvent

    private val _deleteShoppingListById = MutableLiveData<DeleteShoppingListValue>()
    val deleteShoppingListByIdLiveData = Transformations.switchMap(_deleteShoppingListById) {
        mDeleteShoppingListByIdUseCase.invoke(it)
    }

    fun addShoppingListEvent() {
        _addShoppingListEvent.postValue(StateHandler.success(Unit))
    }

    fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue) {
        _deleteShoppingListById.postValue(deleteShoppingListValue)
    }
}