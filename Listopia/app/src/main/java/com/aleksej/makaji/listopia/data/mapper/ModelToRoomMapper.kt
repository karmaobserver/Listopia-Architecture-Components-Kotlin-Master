package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.room.model.ShoppingList

/**
 * Created by Aleksej Makaji on 1/19/19.
 */
object ModelToRoomMapper {
    fun mapShoppingList(shoppingListModel: ShoppingListModel): ShoppingList {
        return ShoppingList(shoppingListModel.id,
                shoppingListModel.name,
                shoppingListModel.ownerUid)
    }
}