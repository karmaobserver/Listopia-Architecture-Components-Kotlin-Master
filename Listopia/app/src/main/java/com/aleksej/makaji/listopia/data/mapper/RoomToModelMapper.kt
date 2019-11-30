package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.room.model.*
import com.aleksej.makaji.listopia.util.mapTo

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
fun ShoppingListWithEditors.mapToShoppingListModel(): ShoppingListModel = mapTo<ShoppingListModel>().copy(id = shoppingList.id, name = shoppingList.name, ownerId = shoppingList.ownerId,
        isSynced = shoppingList.isSynced, timestamp = shoppingList.timestamp, editors = editors?.map { it.mapToUserModel() }, products = products?.map { it.mapToProductModel() })

fun ShoppingList.mapToShoppingListModel(): ShoppingListModel = mapTo<ShoppingListModel>().copy()

fun Product.mapToProductModel(): ProductModel = mapTo<ProductModel>().copy()

fun User.mapToUserModel(): UserModel = mapTo<UserModel>().copy()

fun UserWithFriends.mapToUserModel(): UserModel = mapTo<UserModel>().copy(id = user.id, name = user.name, avatar = user.avatar, friends = friends.map { it.mapToUserModel()})