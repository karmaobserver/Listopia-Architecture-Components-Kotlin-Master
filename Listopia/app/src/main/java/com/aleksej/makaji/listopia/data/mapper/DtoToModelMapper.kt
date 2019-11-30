package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.api.dto.response.ProductResponse
import com.aleksej.makaji.listopia.data.api.dto.response.ShoppingListResponse
import com.aleksej.makaji.listopia.data.api.dto.response.UserResponse
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.util.mapTo

/**
 * Created by Aleksej Makaji on 5/5/19.
 */
fun UserResponse.mapToUserModel(): UserModel = mapTo<UserModel>().copy()

fun ShoppingListResponse.mapToShoppingListModel(): ShoppingListModel = mapTo<ShoppingListModel>().copy(isSynced = false)

fun ProductResponse.mapToProductModel(): ProductModel = mapTo<ProductModel>().copy(isSynced = false)

