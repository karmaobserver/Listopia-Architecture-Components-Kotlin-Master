package com.aleksej.makaji.listopia.data.repository.model

import android.net.Uri

/**
 * Created by Aleksej Makaji on 1/6/19.
 */
data class UserModel(val uid: String = "",
                     val name: String? = null,
                     val email: String? = null,
                     val avatar: Uri? = null)