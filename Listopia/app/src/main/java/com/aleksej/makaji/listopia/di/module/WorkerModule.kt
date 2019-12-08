package com.aleksej.makaji.listopia.di.module

import androidx.work.ListenableWorker
import com.aleksej.makaji.listopia.di.annotation.WorkerKey
import com.aleksej.makaji.listopia.worker.IWorkerFactory
import com.aleksej.makaji.listopia.worker.ShoppingListSyncronizeWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Aleksej Makaji on 2019-12-08.
 */
@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(ShoppingListSyncronizeWorker::class)
    fun bindShoppingListCreateWorker(factory: ShoppingListSyncronizeWorker.Factory): IWorkerFactory<out ListenableWorker>

}