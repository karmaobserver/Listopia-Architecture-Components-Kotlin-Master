package com.aleksej.makaji.listopia.worker

import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

/**
 * Created by Aleksej Makaji on 2019-12-08.
 */
interface IWorkerFactory<T : ListenableWorker> {
    fun create(params: WorkerParameters): T
}