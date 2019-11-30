package com.aleksej.makaji.listopia.data.api.dto.request

import java.util.*

/**
 * Created by Aleksej Makaji on 2019-11-30.
 */
data class SaveProductRequest(val id: String,
                              var name: String,
                              var quantity: Double,
                              var unit: String,
                              var price: Double,
                              var notes: String,
                              var isDeleted: Boolean,
                              var isChecked: Boolean,
                              val shoppingListId: String,
                              val timestamp: Date)