package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.room.model.Product
import com.aleksej.makaji.listopia.data.room.model.ShoppingList
import com.aleksej.makaji.listopia.data.room.model.ShoppingListUserXRef
import com.aleksej.makaji.listopia.data.room.model.User
import com.aleksej.makaji.listopia.data.usecase.value.*
import com.aleksej.makaji.listopia.util.mapTo
import java.util.*

/**
 * Created by Aleksej Makaji on 1/19/19.
 */
fun SaveProductValue.mapToProduct(): Product = mapTo<Product>().copy(id = UUID.randomUUID().toString(), isSynced = false, isDeleted = false, timestamp = Date())

fun SaveShoppingListValue.mapToShoppingList(): ShoppingList = mapTo<ShoppingList>().copy(isSynced = false, isDeleted = false, timestamp = Date())

fun SaveUserValue.mapToUser(): User = mapTo<User>().copy()

fun SaveShoppingListEditorValue.mapToShoppingListEditor(): ShoppingListUserXRef = mapTo<ShoppingListUserXRef>().copy()