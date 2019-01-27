package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import kotlinx.coroutines.Deferred

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
interface ShoppingListDataSource {
    fun getShoppingLists() : LiveData<StateHandler<PagedList<ShoppingListModel>>>
    fun getShoppingList() : LiveData<StateHandler<ShoppingListModel>>
    suspend fun saveShoppingList(shoppingListValue: ShoppingListValue): Deferred<State<Long>>
    suspend fun deleteAllShoppingLists(): Deferred<State<Int>>
}