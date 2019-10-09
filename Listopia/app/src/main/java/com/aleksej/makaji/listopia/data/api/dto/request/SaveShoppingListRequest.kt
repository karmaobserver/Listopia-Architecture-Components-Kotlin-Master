package com.aleksej.makaji.listopia.data.api.dto.request

import java.util.*

/**
 * Created by Aleksej Makaji on 2019-10-07.
 */
data class SaveShoppingListRequest(val id: String,
                                   val name: String,
                                   val ownerId: String,
                                   val timestamp: Date,
                                   val editors: List<String>)