package com.aleksej.makaji.listopia.data.usecase

import android.content.Context
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.error.UnknownError
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.aleksej.makaji.listopia.util.Validator
import com.aleksej.makaji.listopia.util.isConnectedToNetwork
import com.aleksej.makaji.listopia.worker.WorkerUtil
import java.util.*
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
class UpdateShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository,
                                                    private val mSharedPreferenceManager: SharedPreferenceManager,
                                                    private val mContext: Context) : UseCase<ShoppingListModel, Int>() {

    override suspend fun invoke(value: ShoppingListModel): State<Int> {
        Validator.validateListName(value.name)?.run {
            return ErrorState(this)
        }
        var returnValue : State<Int> = ErrorState(UnknownError)
        value.timestamp = Date()
        value.isSynced = false
        when (val updateShoppingListRoom = mShoppingListRepository.updateShoppingList(value)) {
            is SuccessState -> {
                if (mSharedPreferenceManager.userId.isBlank()) return updateShoppingListRoom
                if (!mContext.isConnectedToNetwork()) {
                    WorkerUtil.createShoppingListSynchronizeWorker(mContext)
                    return SuccessState(1)
                } else {
                    when (val updateShoppingListRemote  = mShoppingListRepository.updateShoppingListRemote(value)) {
                        is SuccessState -> {
                            mShoppingListRepository.updateSyncShoppingList(value.id)
                            return updateShoppingListRoom
                        }
                        is LoadingState -> return LoadingState()
                        is ErrorState -> { return ErrorState(updateShoppingListRemote.error) }
                    }
                }
            }
            else -> returnValue = updateShoppingListRoom
        }
        return returnValue
    }
}