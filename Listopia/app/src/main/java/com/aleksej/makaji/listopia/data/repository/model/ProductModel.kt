package com.aleksej.makaji.listopia.data.repository.model

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
data class ProductModel(val id: String,
                        var name: String,
                        var quantity: Double,
                        var unit: String,
                        var price: Double,
                        var notes: String,
                        var isDeleted: Boolean,
                        var isChecked: Boolean,
                        val shoppingListId: Long)