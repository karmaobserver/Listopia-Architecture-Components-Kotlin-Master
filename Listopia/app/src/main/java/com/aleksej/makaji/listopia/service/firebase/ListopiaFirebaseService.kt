package com.aleksej.makaji.listopia.service.firebase

import com.aleksej.makaji.listopia.data.enums.NotificationConstants
import com.aleksej.makaji.listopia.data.enums.NotificationConstants.Companion.CONTENT_PRODUCT_ID
import com.aleksej.makaji.listopia.data.enums.NotificationConstants.Companion.CONTENT_SHOPPING_LIST_ID
import com.aleksej.makaji.listopia.data.usecase.FetchAndSaveProductUseCase
import com.aleksej.makaji.listopia.data.usecase.FetchAndSaveShoppingListUseCase
import com.aleksej.makaji.listopia.data.usecase.UpdateFirebaseTokenUseCase
import com.aleksej.makaji.listopia.data.usecase.value.FetchAndSaveProductValue
import com.aleksej.makaji.listopia.data.usecase.value.FetchAndSaveShoppingListValue
import com.aleksej.makaji.listopia.util.safeLet
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

    @Inject
    lateinit var mFetchAndSaveShoppingListUseCase: FetchAndSaveShoppingListUseCase

    @Inject
    lateinit var mFetchAndSaveProductUseCase: FetchAndSaveProductUseCase

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
                    val shoppingListId = data[CONTENT_SHOPPING_LIST_ID]
                    shoppingListId?.let {
                        mFetchAndSaveShoppingListUseCase.invoke(FetchAndSaveShoppingListValue(it))
                    }
                }
                NotificationConstants.PRODUCT_UPDATED -> {
                    val shoppingListId = data[CONTENT_SHOPPING_LIST_ID]
                    val productId = data[CONTENT_PRODUCT_ID]
                    safeLet(shoppingListId, productId) { shoppingListId, productId ->
                        GlobalScope.launch {
                            mFetchAndSaveProductUseCase.invoke(FetchAndSaveProductValue(shoppingListId, productId))
                        }
                    }
                }
            }
        }
    }
}