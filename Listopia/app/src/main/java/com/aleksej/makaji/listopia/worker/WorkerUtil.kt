package com.aleksej.makaji.listopia.worker

import android.content.Context
import androidx.work.*

/**
 * Created by Aleksej Makaji on 2019-12-08.
 */
object WorkerUtil {

    private const val SHOPPING_LIST_SYNCHRONIZE_WORKER = "SHOPPING_LIST_SYNCHRONIZE_WORKER"
    private const val PRODUCT_SYNCHRONIZE_WORKER = "PRODUCT_SYNCHRONIZE_WORKER"

    fun createShoppingListSynchronizeWorker(context: Context) {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val request = OneTimeWorkRequestBuilder<ShoppingListSynchronizeWorker>()
                .addTag(SHOPPING_LIST_SYNCHRONIZE_WORKER)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context).enqueue(request)
    }

    fun createProductSynchronizeWorker(context: Context) {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val request = OneTimeWorkRequestBuilder<ProductSynchronizeWorker>()
                .addTag(PRODUCT_SYNCHRONIZE_WORKER)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}