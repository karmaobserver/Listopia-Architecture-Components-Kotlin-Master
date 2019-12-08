package com.aleksej.makaji.listopia.di.annotation

import androidx.work.ListenableWorker
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Created by Aleksej Makaji on 2019-12-08.
 */

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)