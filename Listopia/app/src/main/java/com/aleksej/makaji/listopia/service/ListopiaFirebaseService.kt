package com.aleksej.makaji.listopia.service

import com.aleksej.makaji.listopia.data.enums.NotificationConstants
import com.aleksej.makaji.listopia.data.usecase.UpdateFirebaseTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-01.
 */
class ListopiaFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var mUpdateFirebaseTokenUseCase: UpdateFirebaseTokenUseCase

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        GlobalScope.launch {
            mUpdateFirebaseTokenUseCase.invoke(Unit)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage?.data?.get(NotificationConstants.NOTIFICATION)?.run {
            handleMessage(this, remoteMessage.data)
        }
    }

    private fun handleMessage(notification: String, data: Map<String, String>) {
        GlobalScope.launch {
            when (notification) {
                NotificationConstants.SHOPPING_LIST_UPDATED -> {
                    "caeda"
                }
                NotificationConstants.PRODUCT_UPDATED -> {}
                NotificationConstants.USER_UPDATED -> {}
                NotificationConstants.SHOPPING_LIST_DELETED -> {}
                NotificationConstants.PRODUCT_DELETED -> {}
                NotificationConstants.SHOPPING_LIST_SHARE_ALLOWED -> {}
                NotificationConstants.SHOPPING_LIST_SHARE_CANCEL -> {}
            }
        }
    }
}