package com.aleksej.makaji.listopia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.mapper.mapToShoppingListValue
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.*
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/27/19.
 */
class ShoppingListViewModel @Inject constructor(private val mGetShoppingListsUseCase: GetShoppingListsUseCase,
                                                private val mDeleteShoppingListByIdUseCase: DeleteShoppingListByIdUseCase,
                                                private val mSaveShoppingListUseCase: SaveShoppingListUseCase,
                                                private val mUpdateShoppingListUseCase: UpdateShoppingListUseCase,
                                                private val mGetShoppingListByIdUseCase: GetShoppingListByIdUseCase,
                                                private val mShoppingListRepository: ShoppingListRepository) : ViewModel() {

    var reloadEditData = true

    private val _getShoppingLists = MutableLiveData<Unit>()
    val getShoppingListsLiveData = Transformations.switchMap(_getShoppingLists) {
        mGetShoppingListsUseCase.invoke(Unit)
    }

    private val _addShoppingListEvent = MutableLiveData<StateHandler<Unit>>()
    val addShoppingListEvent : LiveData<StateHandler<Unit>>
        get() = _addShoppingListEvent

    private val _deleteShoppingListById = MutableLiveData<DeleteShoppingListValue>()
    val deleteShoppingListByIdLiveData = Transformations.switchMap(_deleteShoppingListById) {
        mDeleteShoppingListByIdUseCase.invoke(it)
    }

    private val _saveShoppingList = MutableLiveData<SaveShoppingListValue>()
    val saveShoppingListLiveData = Transformations.switchMap(_saveShoppingList) {
        mSaveShoppingListUseCase.invoke(it)
    }

    private val _updateShoppingList = MutableLiveData<ShoppingListValue>()
    val updateShoppingListLiveData = Transformations.switchMap(_updateShoppingList) {
        mUpdateShoppingListUseCase.invoke(it)
    }

    private val _getShoppingListById = MutableLiveData<ShoppingListByIdValue>()
    val getShoppingListByIdLiveData = Transformations.switchMap(_getShoppingListById) {
        mGetShoppingListByIdUseCase.invoke(it)
    }

    fun getShoppingLists() {
        _getShoppingLists.postValue(Unit)
    }

    fun updateShoppingList(shoppingListModel: ShoppingListModel) {
        _updateShoppingList.postValue(shoppingListModel.mapToShoppingListValue())
    }

    fun getShoppingListById(shoppingListByIdValue: ShoppingListByIdValue) {
        _getShoppingListById.postValue(shoppingListByIdValue)
    }

    fun createShoppingList(saveShoppingListValue: SaveShoppingListValue) {
        _saveShoppingList.postValue(saveShoppingListValue)
    }

    fun addShoppingListEvent() {
        _addShoppingListEvent.postValue(StateHandler.success(Unit))
        test()
    }

    fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue) {
        _deleteShoppingListById.postValue(deleteShoppingListValue)
    }

    fun test() {
        GlobalScope.launch {
            val result = mShoppingListRepository.fetchShoppingLists()
            when (result) {
                is State.Success -> {
                    Log.d("RETRO", "SUCCESS")
                }
                is State.Error -> {
                    Log.d("RETRO", "ERROR" + result.error)
                }
            }
        }
    }
}