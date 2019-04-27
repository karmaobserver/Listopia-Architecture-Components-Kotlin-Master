package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import com.aleksej.makaji.listopia.util.Validator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
class UpdateShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<ShoppingListValue, LiveData<StateHandler<Int>>> {

    private val useCaseLiveData = MutableLiveData<StateHandler<Int>>()

    override fun invoke(value: ShoppingListValue): LiveData<StateHandler<Int>> {
        Validator.validateListName(value.name)?.let {
            useCaseLiveData.postValue(StateHandler.error(it))
            return useCaseLiveData
        }
        GlobalScope.launch {
            useCaseLiveData.postValue(StateHandler.loading())
            val shoppingListResponse = mShoppingListRepository.updateShoppingList(value).await()
            useCaseLiveData.postValue(StateHandler(shoppingListResponse))
        }
        return useCaseLiveData
    }
}