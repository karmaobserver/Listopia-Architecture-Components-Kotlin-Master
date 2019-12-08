package com.aleksej.makaji.listopia.worker

import android.content.Context
import androidx.work.*

/**
 * Created by Aleksej Makaji on 2019-12-08.
 */
object WorkerUtil {

    private const val SHOPPING_LIST_CREATE_WORKER = "SHOPPING_LIST_CREATE_WORKER"

    const val SHOPPING_LIST_ID_WORKER = "SHOPPING_LIST_ID_WORKER"

    fun createShoppingListCreateWorker(context: Context, shoppingListId: String) {
        val data = workDataOf(SHOPPING_LIST_ID_WORKER to shoppingListId)

        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val request = OneTimeWorkRequestBuilder<ShoppingListCreateWorker>()
                .addTag(SHOPPING_LIST_CREATE_WORKER)
                .setConstraints(constraints)
                .setInputData(data)
                .build()

        WorkManager.getInstance(context).enqueue(request)
    }

}