package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
interface ShoppingListDataSource {
    fun getShoppingLists() : LiveData<StateHandler<PagedList<ShoppingListModel>>>
    fun getShoppingList() : LiveData<StateHandler<ShoppingListModel>>
}