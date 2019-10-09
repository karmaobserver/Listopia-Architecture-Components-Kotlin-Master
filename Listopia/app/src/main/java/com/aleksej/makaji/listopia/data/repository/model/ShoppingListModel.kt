package com.aleksej.makaji.listopia.data.repository.model

import java.util.*

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
data class ShoppingListModel(val id: String,
                             var name: String,
                             val ownerId: String,
                             val timestamp: Date)