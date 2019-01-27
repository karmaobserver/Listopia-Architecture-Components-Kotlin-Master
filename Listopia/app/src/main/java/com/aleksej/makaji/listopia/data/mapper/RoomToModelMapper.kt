package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.room.model.Product
import com.aleksej.makaji.listopia.data.room.model.ShoppingList

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
object RoomToModelMapper {
    fun mapShoppingList(shoppingList: ShoppingList): ShoppingListModel {
        return ShoppingListModel(shoppingList.id,
                shoppingList.name,
                shoppingList.ownerUid)
    }

    fun mapProduct(product: Product): ProductModel {
        return ProductModel(product.id,
                product.name,
                product.quantity,
                product.unit,
                product.price,
                product.notes,
                product.isChecked,
                product.shoppingListId)
    }
}