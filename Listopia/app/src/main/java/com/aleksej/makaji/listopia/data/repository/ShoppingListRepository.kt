package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.*
import com.aleksej.makaji.listopia.di.annotation.Local
import com.aleksej.makaji.listopia.di.annotation.Remote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Singleton
class ShoppingListRepository @Inject constructor(@Remote private val mRemoteShoppingListDataSource: ShoppingListDataSource,
                                                 @Local private val mLocalShoppingListDataSource: ShoppingListDataSource): ShoppingListDataSource {

    override suspend fun saveShoppingListRemote(shoppingListModel: ShoppingListModel): State<Unit> {
        return mLocalShoppingListDataSource.saveShoppingListRemote(shoppingListModel)
    }

    override suspend fun updateSyncShoppingList(shoppingListId: String): State<Int> {
        return mLocalShoppingListDataSource.updateSyncShoppingList(shoppingListId)
    }

    override fun getShoppingLists(): LiveData<StateHandler<PagedList<ShoppingListModel>>> {
        return mLocalShoppingListDataSource.getShoppingLists()
    }

    override fun getShoppingListById(shoppingListByIdValue: ShoppingListByIdValue): LiveData<StateHandler<ShoppingListModel>> {
        return mLocalShoppingListDataSource.getShoppingListById(shoppingListByIdValue)
    }

    override suspend fun getShoppingListByIdSuspend(shoppingListByIdValue: ShoppingListByIdValue): State<ShoppingListModel> {
        return mLocalShoppingListDataSource.getShoppingListByIdSuspend(shoppingListByIdValue)
    }

    override suspend fun saveShoppingList(saveShoppingListValue: SaveShoppingListValue): State<Long> {
        return mLocalShoppingListDataSource.saveShoppingList(saveShoppingListValue)
    }

    override suspend fun deleteAllShoppingLists(): State<Int> {
        return mLocalShoppingListDataSource.deleteAllShoppingLists()
    }

    override suspend fun updateShoppingList(shoppingListValue: ShoppingListValue): State<Int> {
        return mLocalShoppingListDataSource.updateShoppingList(shoppingListValue)
    }

    override suspend fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue): State<Int> {
        return mLocalShoppingListDataSource.deleteShoppingListById(deleteShoppingListValue)
    }

    override suspend fun fetchShoppingLists(): State<Unit> {
        return mRemoteShoppingListDataSource.fetchShoppingLists()
    }

    override suspend fun fetchShoppingListsByUserId(fetchShoppingListsValue: FetchShoppingListsValue): State<List<ShoppingListModel>> {
        return mRemoteShoppingListDataSource.fetchShoppingListsByUserId(fetchShoppingListsValue)
    }
}