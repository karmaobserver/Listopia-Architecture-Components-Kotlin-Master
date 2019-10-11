package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.room.model.Product
import com.aleksej.makaji.listopia.data.room.model.ShoppingList
import com.aleksej.makaji.listopia.data.room.model.User
import com.aleksej.makaji.listopia.data.room.model.UserWithFriends
import com.aleksej.makaji.listopia.util.mapTo

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
fun ShoppingList.mapToShoppingListModel(): ShoppingListModel = mapTo<ShoppingListModel>().copy()

fun Product.mapToProductModel(): ProductModel = mapTo<ProductModel>().copy()

fun User.mapToUserModel(): UserModel = mapTo<UserModel>().copy()

fun UserWithFriends.mapToUserModel(): UserModel = mapTo<UserModel>().copy(id = user.id, name = user.name, email = user.email, avatar = user.avatar, friends = friends.map { it.mapToUserModel()})