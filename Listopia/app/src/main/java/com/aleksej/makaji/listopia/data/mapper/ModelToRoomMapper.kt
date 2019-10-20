package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.room.model.*
import com.aleksej.makaji.listopia.util.mapTo

/**
 * Created by Aleksej Makaji on 2/3/19.
 */
fun ProductModel.mapToProduct(): Product = mapTo<Product>().copy()

fun ShoppingListModel.mapToShoppingList(): ShoppingList = mapTo<ShoppingList>().copy()

fun UserModel.mapToUser(): User = mapTo<User>().copy()

fun UserModel.mapToUserWithFriends(): UserWithFriends = mapTo<UserWithFriends>().copy()