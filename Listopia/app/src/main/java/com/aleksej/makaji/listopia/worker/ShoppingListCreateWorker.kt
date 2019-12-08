package com.aleksej.makaji.listopia.worker

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-08.
 */
class ShoppingListCreateWorker(context: Context, params: WorkerParameters, private val mShoppingListRepository: ShoppingListRepository): Worker(context, params) {
    override fun doWork(): Result {
        return heavyWork()
    }

    private fun heavyWork(): Result = runBlocking(Dispatchers.IO) {
        val shoppingListId = inputData.getString(WorkerUtil.SHOPPING_LIST_ID_WORKER)
        shoppingListId?.let {
            when (val getShoppingListRoom = mShoppingListRepository.getShoppingListByIdSuspend(ShoppingListByIdValue(shoppingListId))) {
                is SuccessState -> {
                    getShoppingListRoom.data?.let {
                        when (mShoppingListRepository.saveShoppingListRemote(it)) {
                            is SuccessState -> {
                                mShoppingListRepository.updateSyncShoppingList(it.id)
                                Result.success()
                            }
                            is LoadingState -> {}
                            is ErrorState -> {
                                Result.retry()
                            }
                        }
                    }
                }
                is LoadingState -> {}
                is ErrorState -> Result.retry()
            }
        }
        Result.failure()
    }

    class Factory @Inject constructor(
            private val context: Context,
            private val shoppingListRepository: ShoppingListRepository
    ) : IWorkerFactory<ShoppingListCreateWorker> {
        override fun create(params: WorkerParameters): ShoppingListCreateWorker {
            return ShoppingListCreateWorker(context, params, shoppingListRepository)
        }
    }
}