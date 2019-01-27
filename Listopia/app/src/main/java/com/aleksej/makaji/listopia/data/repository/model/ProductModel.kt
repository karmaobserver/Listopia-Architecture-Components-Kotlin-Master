package com.aleksej.makaji.listopia.data.repository.model

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
data class ProductModel(val id: Long,
                        val name: String,
                        val quantity: Double,
                        val unit: String,
                        val price: Double,
                        val notes: String,
                        val isChecked: Boolean,
                        val shoppingListId: Long)