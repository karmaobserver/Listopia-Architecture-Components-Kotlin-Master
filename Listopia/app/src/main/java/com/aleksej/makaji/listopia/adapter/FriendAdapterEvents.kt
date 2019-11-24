package com.aleksej.makaji.listopia.adapter

import android.view.View
import com.aleksej.makaji.listopia.data.repository.model.UserModel

/**
 * Created by Aleksej Makaji on 2019-10-10.
 */
sealed class FriendAdapterEvents {
    data class FriendClick(val userModel: UserModel, val shouldAddEditor: Boolean) : FriendAdapterEvents()
    data class OptionsClick(val view: View, val userModel: UserModel) : FriendAdapterEvents()
}