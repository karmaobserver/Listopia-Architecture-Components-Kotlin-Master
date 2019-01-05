package com.aleksej.makaji.listopia.data.repository.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ShoppingListDataSource
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Singleton
class ShoppingListRemoteDataSource @Inject constructor() : ShoppingListDataSource {
    override fun getShoppingList(): LiveData<StateHandler<ShoppingListModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShoppingLists(): LiveData<StateHandler<PagedList<ShoppingListModel>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}