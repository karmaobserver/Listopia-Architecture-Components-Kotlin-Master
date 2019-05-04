package com.aleksej.makaji.listopia.data.usecase.value

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
data class SaveUserValue(val uid: String,
                         val name: String? = null,
                         val email: String? = null,
                         val avatar: String? = null,
                         val sourceType: String)