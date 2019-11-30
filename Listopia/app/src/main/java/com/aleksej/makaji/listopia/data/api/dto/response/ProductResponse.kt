package com.aleksej.makaji.listopia.data.api.dto.response

import com.aleksej.makaji.listopia.data.mapper.IDtoModelMapper
import com.aleksej.makaji.listopia.data.mapper.mapToProductModel
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import java.util.*

/**
 * Created by Aleksej Makaji on 2019-11-30.
 */
data class ProductResponse(val id: String,
                                var name: String,
                                var quantity: Double,
                                var unit: String,
                                var price: Double,
                                var notes: String,
                                var isDeleted: Boolean,
                                var isChecked: Boolean,
                                val shoppingListId: String,
                                val timestamp: Date) : IDtoModelMapper<ProductResponse, ProductModel> {
    override fun map(value: ProductResponse): ProductModel {
        return value.mapToProductModel()
    }
}