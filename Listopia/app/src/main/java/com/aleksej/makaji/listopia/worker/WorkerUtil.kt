package com.aleksej.makaji.listopia.worker

import android.content.Context
import androidx.work.*

/**
 * Created by Aleksej Makaji on 2019-12-08.
 */
object WorkerUtil {

    private const val SHOPPING_LIST_CREATE_WORKER = "SHOPPING_LIST_CREATE_WORKER"

    fun createShoppingListSyncronizeWorker(context: Context) {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val request = OneTimeWorkRequestBuilder<ShoppingListSyncronizeWorker>()
                .addTag(SHOPPING_LIST_CREATE_WORKER)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context).enqueue(request)
    }

}