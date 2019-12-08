package com.aleksej.makaji.listopia.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-08.
 */
class ShoppingListSynchronizeWorker(context: Context,
                                    params: WorkerParameters,
                                    private val mShoppingListRepository: ShoppingListRepository,
                                    private val mSharedPreferenceManager: SharedPreferenceManager): Worker(context, params) {
    override fun doWork(): Result {
        return heavyWork()
    }

    private fun heavyWork(): Result = runBlocking(Dispatchers.IO) {
        when (val getShoppingListsNotSyncedRoom = mShoppingListRepository.getShoppingListsNotSyncedSuspend()) {
            is SuccessState -> {
                getShoppingListsNotSyncedRoom.data?.let {
                    if (it.isEmpty()) return@runBlocking Result.success()

                    val shoppingListIds = arrayListOf<String>()
                    val roomShoppingLists = it
                    roomShoppingLists.forEach {
                        shoppingListIds.add(it.id)
                        if (it.ownerId == "") {
                            it.ownerId = mSharedPreferenceManager.userId
                        }
                    }

                    when (mShoppingListRepository.saveOrUpdateShoppingListsRemote(roomShoppingLists)) {
                        is SuccessState -> {
                            mShoppingListRepository.updateSyncShoppingLists(shoppingListIds)
                            return@runBlocking Result.success()
                        }
                        is LoadingState -> {}
                        is ErrorState -> {
                            return@runBlocking Result.failure()
                        }
                    }
                }
            }
            is LoadingState -> {}
            is ErrorState -> return@runBlocking Result.failure()
        }
        Result.failure()
    }

    class Factory @Inject constructor(
            private val context: Context,
            private val shoppingListRepository: ShoppingListRepository,
            private val sharedPreferenceManager: SharedPreferenceManager
    ) : IWorkerFactory<ShoppingListSynchronizeWorker> {
        override fun create(params: WorkerParameters): ShoppingListSynchronizeWorker {
            return ShoppingListSynchronizeWorker(context, params, shoppingListRepository, sharedPreferenceManager)
        }
    }
}