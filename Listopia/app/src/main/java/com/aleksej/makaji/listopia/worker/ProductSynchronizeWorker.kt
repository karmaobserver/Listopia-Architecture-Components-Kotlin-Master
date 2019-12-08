package com.aleksej.makaji.listopia.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-08.
 */
class ProductSynchronizeWorker(context: Context,
                               params: WorkerParameters,
                               private val mShoppingListRepository: ShoppingListRepository,
                               private val mProductRepository: ProductRepository,
                               private val mSharedPreferenceManager: SharedPreferenceManager): Worker(context, params) {
    override fun doWork(): Result {
        return heavyWork()
    }

    private fun heavyWork(): Result = runBlocking(Dispatchers.IO) {
        when (val getProductsNotSyncedRoom = mProductRepository.getProductsNotSyncedSuspend()) {
            is SuccessState -> {
                getProductsNotSyncedRoom.data?.let {
                    if (it.isEmpty()) return@runBlocking Result.success()

                    val productIds = arrayListOf<String>()
                    val roomProducts = it
                    roomProducts.forEach {
                        productIds.add(it.id)
                    }

                    when (mProductRepository.saveOrUpdateProductsRemote(roomProducts)) {
                        is SuccessState -> {
                            mProductRepository.updateSyncProducts(productIds)
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
            private val productRepository: ProductRepository,
            private val sharedPreferenceManager: SharedPreferenceManager
    ) : IWorkerFactory<ProductSynchronizeWorker> {
        override fun create(params: WorkerParameters): ProductSynchronizeWorker {
            return ProductSynchronizeWorker(context, params, shoppingListRepository, productRepository, sharedPreferenceManager)
        }
    }
}