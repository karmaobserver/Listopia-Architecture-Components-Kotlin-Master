package com.aleksej.makaji.listopia.data.usecase

import android.util.Log
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import com.aleksej.makaji.listopia.error.UnknownError
import com.aleksej.makaji.listopia.util.Validator
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/19/19.
 */
class SaveShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<SaveShoppingListValue, Long>() {

    override suspend fun invoke(value: SaveShoppingListValue): State<Long> {
        Validator.validateListName(value.name)?.run {
            return ErrorState(this)
        }
        var returnValue : State<Long> = ErrorState(UnknownError)
        when (val saveShoppingListRoom = mShoppingListRepository.saveShoppingList(value)) {
            is SuccessState -> {
                when (val getShoppingListRoom = mShoppingListRepository.getShoppingListByIdSuspend(ShoppingListByIdValue(value.id))) {
                    is SuccessState -> {
                        getShoppingListRoom.data?.let {
                            when (val saveShoppingListRemote = mShoppingListRepository.saveShoppingListRemote(it)) {
                                is SuccessState -> {
                                    mShoppingListRepository.updateSyncShoppingList(it.id)
                                    return saveShoppingListRoom
                                }
                                else -> {
                                    returnValue =  saveShoppingListRoom
                                    Log.d("SaveShoppingListUseCase", "error: saveShoppingListRemote")
                                }
                            }
                        }
                    }
                    else -> {
                        returnValue = saveShoppingListRoom
                        Log.d("SaveShoppingListUseCase", "error: getShoppingListRoom")
                    }
                }
            }
            else -> returnValue = saveShoppingListRoom
        }
        return returnValue
    }
}