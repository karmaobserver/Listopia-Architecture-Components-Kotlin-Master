package com.aleksej.makaji.listopia.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.mapper.mapToShoppingListValue
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.*
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/27/19.
 */
class ShoppingListViewModel @Inject constructor(private val mDeleteShoppingListByIdUseCase: DeleteShoppingListByIdUseCase,
                                                private val mSaveShoppingListUseCase: SaveShoppingListUseCase,
                                                private val mUpdateShoppingListUseCase: UpdateShoppingListUseCase,
                                                private val mShoppingListRepository: ShoppingListRepository) : ViewModel() {

    var reloadEditData = true

    private val getShoppingListsTrigger = MutableLiveData<Unit>()
    val getShoppingListsLiveData = Transformations.switchMap(getShoppingListsTrigger) { mShoppingListRepository.getShoppingLists() }

    private val getShoppingListByIdTrigger = MutableLiveData<ShoppingListByIdValue>()
    val getShoppingListByIdLiveData = Transformations.switchMap(getShoppingListByIdTrigger) { mShoppingListRepository.getShoppingListById(it) }

    private val deleteShoppingListByIdTrigger = MutableLiveData<StateHandler<Int>>()
    val deleteShoppingListByIdLiveData : LiveData<StateHandler<Int>> = deleteShoppingListByIdTrigger

    private val saveShoppingListTrigger = MutableLiveData<StateHandler<Long>>()
    val saveShoppingListLiveData : LiveData<StateHandler<Long>> = saveShoppingListTrigger

    private val updateShoppingListTrigger = MutableLiveData<StateHandler<Int>>()
    val updateShoppingListLiveData : LiveData<StateHandler<Int>> = updateShoppingListTrigger

    private val addShoppingListEventTrigger = MutableLiveData<StateHandler<Unit>>()
    val addShoppingListEvent : LiveData<StateHandler<Unit>> = addShoppingListEventTrigger

    fun getShoppingLists() {
        getShoppingListsTrigger.postValue(Unit)
    }

    fun updateShoppingList(shoppingListModel: ShoppingListModel) {
        viewModelScope.launch {
            updateShoppingListTrigger.value = StateHandler(mUpdateShoppingListUseCase.invoke(shoppingListModel.mapToShoppingListValue()))
        }
    }

    fun getShoppingListById(shoppingListByIdValue: ShoppingListByIdValue) {
        getShoppingListByIdTrigger.postValue(shoppingListByIdValue)
    }

    fun createShoppingList(saveShoppingListValue: SaveShoppingListValue) {
        viewModelScope.launch {
            saveShoppingListTrigger.value = StateHandler(mSaveShoppingListUseCase.invoke(saveShoppingListValue))
        }
    }

    fun addShoppingListEvent() {
        addShoppingListEventTrigger.postValue(StateHandler.success(Unit))
    }

    fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue) {
        viewModelScope.launch {
            deleteShoppingListByIdTrigger.value = StateHandler(mDeleteShoppingListByIdUseCase.invoke(deleteShoppingListValue))
        }
    }

    fun test() {
        GlobalScope.launch {
            val result = mShoppingListRepository.fetchShoppingLists()
            when (result) {
                is SuccessState -> {
                    Log.d("RETRO", "SUCCESS")
                }
                is ErrorState -> {
                    Log.d("RETRO", "ERROR" + result.error)
                }
            }
        }
    }
}