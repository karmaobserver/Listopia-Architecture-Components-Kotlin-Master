package com.aleksej.makaji.listopia.data.repository.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ShoppingListDataSource
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Singleton
class ShoppingListRemoteDataSource @Inject constructor() : ShoppingListDataSource {

    override fun getShoppingListById(shoppingListByIdValue: ShoppingListByIdValue): LiveData<StateHandler<ShoppingListModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShoppingLists(): LiveData<StateHandler<PagedList<ShoppingListModel>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveShoppingList(saveShoppingListValue: SaveShoppingListValue): Deferred<State<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAllShoppingLists(): Deferred<State<Int>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateShoppingList(shoppingListValue: ShoppingListValue): Deferred<State<Int>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue): Deferred<State<Int>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}