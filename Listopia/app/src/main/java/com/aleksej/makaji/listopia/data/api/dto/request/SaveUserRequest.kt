package com.aleksej.makaji.listopia.data.api.dto.request

import com.aleksej.makaji.listopia.data.repository.model.UserModel

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
data class SaveUserRequest(val id: String = "",
                           val name: String? = null,
                           val email: String? = null,
                           val avatar: String? = null,
                           val friends: List<UserRequest>? = null)