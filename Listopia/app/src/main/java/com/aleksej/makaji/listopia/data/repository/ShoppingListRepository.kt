package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import com.aleksej.makaji.listopia.di.annotation.Local
import com.aleksej.makaji.listopia.di.annotation.Remote
import kotlinx.coroutines.Deferred
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListRepository @Inject constructor(@Remote private val mRemoteShoppingListDataSource: ShoppingListDataSource,
                                                 @Local private val mLocalShoppingListDataSource: ShoppingListDataSource): ShoppingListDataSource {

    override fun getShoppingLists(): LiveData<StateHandler<PagedList<ShoppingListModel>>> {
        return mLocalShoppingListDataSource.getShoppingLists()
    }

    override fun getShoppingListById(shoppingListByIdValue: ShoppingListByIdValue): LiveData<StateHandler<ShoppingListModel>> {
        return mLocalShoppingListDataSource.getShoppingListById(shoppingListByIdValue)
    }

    override suspend fun saveShoppingList(saveShoppingListValue: SaveShoppingListValue): Deferred<State<Long>> {
        return mLocalShoppingListDataSource.saveShoppingList(saveShoppingListValue)
    }

    override suspend fun deleteAllShoppingLists(): Deferred<State<Int>> {
        return mLocalShoppingListDataSource.deleteAllShoppingLists()
    }

    override suspend fun updateShoppingList(shoppingListValue: ShoppingListValue): Deferred<State<Int>> {
        return mLocalShoppingListDataSource.updateShoppingList(shoppingListValue)
    }

    override suspend fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue): Deferred<State<Int>> {
        return mLocalShoppingListDataSource.deleteShoppingListById(deleteShoppingListValue)
    }
}