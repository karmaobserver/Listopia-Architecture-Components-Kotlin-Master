package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.room.model.Product

/**
 * Created by Aleksej Makaji on 2/3/19.
 */
object ModelToRoomMapper {
    fun mapProduct(productModel: ProductModel): Product {
        return Product(productModel.id,
                productModel.name,
                productModel.quantity,
                productModel.unit,
                productModel.price,
                productModel.notes,
                productModel.isChecked,
                productModel.shoppingListId)
    }
}