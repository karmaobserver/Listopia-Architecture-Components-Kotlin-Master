package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.*

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
interface ShoppingListDataSource {
    fun getShoppingLists() : LiveData<StateHandler<PagedList<ShoppingListModel>>>
    fun getShoppingListById(shoppingListByIdValue: ShoppingListByIdValue) : LiveData<StateHandler<ShoppingListModel>>
    suspend fun getShoppingListByIdSuspend(shoppingListByIdValue: ShoppingListByIdValue) : State<ShoppingListModel>
    suspend fun saveShoppingList(saveShoppingListValue: SaveShoppingListValue): State<Long>
    suspend fun saveShoppingListRemote(shoppingListModel: ShoppingListModel): State<Unit>
    suspend fun deleteAllShoppingLists(): State<Int>
    suspend fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue): State<Int>
    suspend fun updateShoppingList(shoppingListValue: ShoppingListValue): State<Int>
    suspend fun updateSyncShoppingList(shoppingListId: String): State<Int>
    suspend fun fetchShoppingLists(): State<Unit>
    suspend fun fetchShoppingListsByUserId(fetchShoppingListsValue: FetchShoppingListsValue): State<List<ShoppingListModel>>
}