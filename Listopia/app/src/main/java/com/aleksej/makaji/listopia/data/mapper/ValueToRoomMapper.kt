package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.room.model.Product
import com.aleksej.makaji.listopia.data.room.model.ShoppingList
import com.aleksej.makaji.listopia.data.room.model.User
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import com.aleksej.makaji.listopia.util.mapTo
import java.util.*

/**
 * Created by Aleksej Makaji on 1/19/19.
 */
fun SaveProductValue.mapToProduct(): Product = mapTo<Product>().copy(id = UUID.randomUUID().toString(), isSynced = false, timestamp = Date())

fun SaveShoppingListValue.mapToShoppingList(): ShoppingList = mapTo<ShoppingList>().copy(isSynced = false, timestamp = Date())

fun ShoppingListValue.mapToShoppingList(): ShoppingList = mapTo<ShoppingList>().copy()

fun SaveUserValue.mapToUser(): User = mapTo<User>().copy()