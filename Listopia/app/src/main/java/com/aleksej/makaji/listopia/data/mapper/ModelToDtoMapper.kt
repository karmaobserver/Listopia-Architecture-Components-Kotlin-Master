package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.api.dto.request.SaveShoppingListRequest
import com.aleksej.makaji.listopia.data.api.dto.request.SaveUserRequest
import com.aleksej.makaji.listopia.data.api.dto.request.UpdateShoppingListRequest
import com.aleksej.makaji.listopia.data.api.dto.request.UserRequest
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.util.mapTo

/**
 * Created by Aleksej Makaji on 2019-10-07.
 */
fun ShoppingListModel.mapToSaveShoppingListRequest(): SaveShoppingListRequest = mapTo<SaveShoppingListRequest>().copy()

fun ShoppingListModel.mapToUpdateShoppingListRequest(): UpdateShoppingListRequest = mapTo<UpdateShoppingListRequest>().copy()

fun UserModel.mapToSaveUserRequest(): SaveUserRequest = mapTo<SaveUserRequest>().copy()

fun UserModel.mapToUserRequest(): UserRequest = mapTo<UserRequest>().copy()