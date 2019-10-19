package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.error.UnknownError
import com.aleksej.makaji.listopia.util.Validator
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
class UpdateShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<ShoppingListModel, Int>() {

    override suspend fun invoke(value: ShoppingListModel): State<Int> {
        Validator.validateListName(value.name)?.run {
            return ErrorState(this)
        }
        var returnValue : State<Int> = ErrorState(UnknownError)
        value.timestamp = Date()
        when (val updateShoppingListRoom = mShoppingListRepository.updateShoppingList(value)) {
            is SuccessState -> {
                value.isSynced = false
                when (val saveShoppingListRemote = mShoppingListRepository.updateShoppingListRemote(value)) {
                    is SuccessState -> {
                        mShoppingListRepository.updateSyncShoppingList(value.id)
                        return updateShoppingListRoom
                    }
                    else -> {
                        returnValue =  updateShoppingListRoom
                        Timber.d( "error: updateShoppingListUseCase")
                    }
                }
            }
            else -> returnValue = updateShoppingListRoom
        }
        return returnValue
    }
}