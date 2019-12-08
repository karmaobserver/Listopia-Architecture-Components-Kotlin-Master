package com.aleksej.makaji.listopia.data.usecase

import android.content.Context
import android.util.Log
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import com.aleksej.makaji.listopia.error.UnknownError
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.aleksej.makaji.listopia.util.Validator
import com.aleksej.makaji.listopia.util.isConnectedToNetwork
import com.aleksej.makaji.listopia.worker.WorkerUtil
import java.lang.Error
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/19/19.
 */
class SaveShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository,
                                                  private val mContext: Context,
                                                  private val mSharedPreferenceManager: SharedPreferenceManager) : UseCase<SaveShoppingListValue, Long>() {

    override suspend fun invoke(value: SaveShoppingListValue): State<Long> {
        Validator.validateListName(value.name)?.run {
            return ErrorState(this)
        }
        var returnValue : State<Long> = ErrorState(UnknownError)
        when (val saveShoppingListRoom = mShoppingListRepository.saveShoppingList(value)) {
            is SuccessState -> {
                if (mSharedPreferenceManager.userId.isBlank()) return saveShoppingListRoom
                when (val getShoppingListRoom = mShoppingListRepository.getShoppingListByIdSuspend(ShoppingListByIdValue(value.id))) {
                    is SuccessState -> {
                        getShoppingListRoom.data?.let {
                            if (!mContext.isConnectedToNetwork()) {
                                WorkerUtil.createShoppingListSyncronizeWorker(mContext)
                                return SuccessState(1L)
                            } else {
                                when (val saveShoppingListRemote = mShoppingListRepository.saveShoppingListRemote(it)) {
                                    is SuccessState -> {
                                        mShoppingListRepository.updateSyncShoppingList(it.id)
                                        return saveShoppingListRoom
                                    }
                                    is LoadingState -> return LoadingState()
                                    is ErrorState -> { return ErrorState(saveShoppingListRemote.error) }
                                }
                            }
                        }
                    }
                    is LoadingState -> return LoadingState()
                    is ErrorState -> return ErrorState(getShoppingListRoom.error)
                }
            }
            else -> returnValue = saveShoppingListRoom
        }
        return returnValue
    }
}