package com.aleksej.makaji.listopia.data.repository.model

/**
 * Created by Aleksej Makaji on 1/6/19.
 */
data class UserModel(val id: String = "",
                     val name: String? = null,
                     val avatar: String? = null,
                     val friends: List<UserModel>? = null)