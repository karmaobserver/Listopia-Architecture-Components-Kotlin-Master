package com.aleksej.makaji.listopia.data.api.dto.request

/**
 * Created by Aleksej Makaji on 2019-10-12.
 */
data class UserRequest(val id: String = "",
                       val name: String? = null,
                       val email: String? = null,
                       val avatar: String? = null)