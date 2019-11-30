package com.aleksej.makaji.listopia.data.usecase.value

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
data class SaveProductValue(val id: String,
                            val name: String,
                            val quantity: Double,
                            val unit: String,
                            val price: Double,
                            val notes: String,
                            val shoppingListId: String)