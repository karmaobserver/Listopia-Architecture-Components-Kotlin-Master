package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
class DeleteShoppingListByIdUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<DeleteShoppingListValue, LiveData<StateHandler<Int>>> {

    private val useCaseLiveData = MutableLiveData<StateHandler<Int>>()

    override fun invoke(value: DeleteShoppingListValue): LiveData<StateHandler<Int>> {
        GlobalScope.launch {
            useCaseLiveData.postValue(StateHandler.loading())
            val deleteShoppingListResponse = mShoppingListRepository.deleteShoppingListById(value)
            useCaseLiveData.postValue(StateHandler(deleteShoppingListResponse))
        }
        return useCaseLiveData
    }
}