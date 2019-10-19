package com.aleksej.makaji.listopia.data.api.dto.request

import java.util.*

/**
 * Created by Aleksej Makaji on 2019-10-13.
 */
data class UpdateShoppingListRequest(val id: String,
                                   val name: String,
                                   val ownerId: String,
                                   val timestamp: Date,
                                   val isDeleted: Boolean,
                                   val editors: List<UserRequest>? = null)