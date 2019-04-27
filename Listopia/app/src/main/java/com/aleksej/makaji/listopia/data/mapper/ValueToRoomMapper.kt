package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.room.model.Product
import com.aleksej.makaji.listopia.data.room.model.ShoppingList
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue

/**
 * Created by Aleksej Makaji on 1/19/19.
 */
object ValueToRoomMapper {
    fun mapShoppingList(shoppingListValue: ShoppingListValue): ShoppingList {
        return ShoppingList(shoppingListValue.id,
                shoppingListValue.name,
                shoppingListValue.ownerUid)
    }

    fun mapSaveShoppingList(saveShoppingListValue: SaveShoppingListValue): ShoppingList {
        return ShoppingList(0,
                saveShoppingListValue.name,
                saveShoppingListValue.ownerUid)
    }

    fun mapProduct(saveProductValue: SaveProductValue): Product {
        return Product(0,
                saveProductValue.name,
                saveProductValue.quantity,
                saveProductValue.unit,
                saveProductValue.price,
                saveProductValue.notes,
                false,
                saveProductValue.shoppingListId)
    }
}