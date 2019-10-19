package com.aleksej.makaji.listopia.data.api.dto.response

import com.aleksej.makaji.listopia.data.mapper.IDtoModelMapper
import com.aleksej.makaji.listopia.data.mapper.mapToShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import java.util.*

/**
 * Created by Aleksej Makaji on 5/5/19.
 */
data class ShoppingListResponse(val id: String,
                                var name: String,
                                val ownerId: String,
                                val isDeleted: Boolean,
                                val timestamp: Date,
                                val editors: List<UserResponse>? = null) : IDtoModelMapper<ShoppingListResponse, ShoppingListModel> {
    override fun map(value: ShoppingListResponse): ShoppingListModel {
        return value.mapToShoppingListModel()
    }
}