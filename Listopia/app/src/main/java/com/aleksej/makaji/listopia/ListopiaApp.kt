package com.aleksej.makaji.listopia

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.aleksej.makaji.listopia.di.AppInjector
import com.aleksej.makaji.listopia.factory.DaggerWorkerFactory
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import dagger.android.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Singleton
class ListopiaApp : Application(), HasActivityInjector, HasServiceInjector, HasBroadcastReceiverInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var serviceAndroidInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var daggerWorkerFactory: DaggerWorkerFactory

    @Inject
    lateinit var broadcastReceiverAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        initializeCrashlytics()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
        configureWorkManager()
    }

    private fun initializeCrashlytics() {
        FirebaseApp.initializeApp(this)
    }

    private fun configureWorkManager() {
        val config = Configuration.Builder()
                .setWorkerFactory(daggerWorkerFactory)
                .build()

        WorkManager.initialize(this, config)
    }

    override fun activityInjector() = dispatchingAndroidInjector

    override fun serviceInjector(): AndroidInjector<Service> = serviceAndroidInjector

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = broadcastReceiverAndroidInjector
}