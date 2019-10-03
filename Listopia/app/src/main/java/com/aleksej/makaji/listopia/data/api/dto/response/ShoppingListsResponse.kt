package com.aleksej.makaji.listopia.data.api.dto.response

import com.aleksej.makaji.listopia.data.mapper.IDtoModelMapper
import com.aleksej.makaji.listopia.data.mapper.mapToShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel

/**
 * Created by Aleksej Makaji on 2019-06-02.
 */
class ShoppingListsResponse : ArrayList<ShoppingListResponse>(), IDtoModelMapper<ShoppingListsResponse, List<ShoppingListModel>> {
    override fun map(value: ShoppingListsResponse): List<ShoppingListModel> {
        return value.map { it.mapToShoppingListModel() }
    }
}