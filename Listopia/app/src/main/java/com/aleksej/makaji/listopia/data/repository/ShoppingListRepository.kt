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
    override suspend fun fetchShoppingListById(shoppingListId: String): State<ShoppingListModel> {
        return mRemoteShoppingListDataSource.fetchShoppingListById(shoppingListId)
    }

    override suspend fun deleteShoppingListByIdRemote(shoppingListId: String): State<Unit> {
        return mRemoteShoppingListDataSource.deleteShoppingListByIdRemote(shoppingListId)
    }

    override suspend fun updateShoppingListRemote(shoppingListModel: ShoppingListModel): State<Unit> {
        return mRemoteShoppingListDataSource.updateShoppingListRemote(shoppingListModel)
    }

    override suspend fun saveShoppingListsWithEditors(saveShoppingListEditorValue: List<SaveShoppingListEditorValue>): State<List<Long>> {
        return mLocalShoppingListDataSource.saveShoppingListsWithEditors(saveShoppingListEditorValue)
    }

    override suspend fun saveShoppingLists(shoppingLists: List<ShoppingListModel>): State<List<Long>> {
        return mLocalShoppingListDataSource.saveShoppingLists(shoppingLists)
    }

    override suspend fun getShoppingListsSuspend(): State<List<ShoppingListModel>> {
        return mLocalShoppingListDataSource.getShoppingListsSuspend()
    }

    override suspend fun saveShoppingListRemote(shoppingListModel: ShoppingListModel): State<Unit> {
        return mRemoteShoppingListDataSource.saveShoppingListRemote(shoppingListModel)
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

    override suspend fun updateShoppingList(shoppingListModel: ShoppingListModel): State<Int> {
        return mLocalShoppingListDataSource.updateShoppingList(shoppingListModel)
    }

    override suspend fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue): State<Int> {
        return mLocalShoppingListDataSource.deleteShoppingListById(deleteShoppingListValue)
    }

    override suspend fun fetchShoppingListsByUserId(fetchShoppingListsValue: FetchShoppingListsValue): State<List<ShoppingListModel>> {
        return mRemoteShoppingListDataSource.fetchShoppingListsByUserId(fetchShoppingListsValue)
    }
}