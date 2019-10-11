package com.aleksej.makaji.listopia.adapter

import com.aleksej.makaji.listopia.data.repository.model.UserModel

/**
 * Created by Aleksej Makaji on 2019-10-10.
 */
sealed class FriendAdapterEvents {
    data class FriendClick(val userModel: UserModel) : FriendAdapterEvents()
    data class ShareClick(val userModel: UserModel) : FriendAdapterEvents()
}