package com.aleksej.makaji.listopia.data.repository.model

import java.util.*

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
data class ShoppingListModel(val id: String,
                             var name: String,
                             var ownerId: String,
                             var isDeleted: Boolean,
                             var isSynced: Boolean,
                             var timestamp: Date,
                             val editors: List<UserModel>? = null,
                             val products: List<ProductModel>? = null)