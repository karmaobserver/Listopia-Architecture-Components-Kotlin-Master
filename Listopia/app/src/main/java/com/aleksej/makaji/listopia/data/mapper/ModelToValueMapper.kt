package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
object ModelToValueMapper {
    fun mapShoppingList(shoppingListModel: ShoppingListModel): ShoppingListValue {
        return ShoppingListValue(shoppingListModel.id,
                shoppingListModel.name,
                shoppingListModel.ownerUid)
    }
}