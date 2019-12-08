package com.aleksej.makaji.listopia.di.module

import androidx.work.ListenableWorker
import com.aleksej.makaji.listopia.di.annotation.WorkerKey
import com.aleksej.makaji.listopia.worker.IWorkerFactory
import com.aleksej.makaji.listopia.worker.ProductSynchronizeWorker
import com.aleksej.makaji.listopia.worker.ShoppingListSynchronizeWorker
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
    @WorkerKey(ShoppingListSynchronizeWorker::class)
    fun bindShoppingListSynchronizeWorkerWorker(factory: ShoppingListSynchronizeWorker.Factory): IWorkerFactory<out ListenableWorker>

    @Binds
    @IntoMap
    @WorkerKey(ProductSynchronizeWorker::class)
    fun bindProductSynchronizeWorker(factory: ProductSynchronizeWorker.Factory): IWorkerFactory<out ListenableWorker>

}