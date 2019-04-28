package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.util.Validator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/19/19.
 */
class SaveShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<SaveShoppingListValue, LiveData<StateHandler<Long>>> {

    private val saveLiveData = MutableLiveData<StateHandler<Long>>()

    override fun invoke(valueSave: SaveShoppingListValue): LiveData<StateHandler<Long>> {
        Validator.validateListName(valueSave.name)?.let {
            saveLiveData.postValue(StateHandler.error(it))
            return saveLiveData
        }
        GlobalScope.launch {
            saveLiveData.postValue(StateHandler.loading())
            val saveShoppingListResponse = mShoppingListRepository.saveShoppingList(valueSave)
            saveLiveData.postValue(StateHandler(saveShoppingListResponse))
        }
        return saveLiveData
    }
}