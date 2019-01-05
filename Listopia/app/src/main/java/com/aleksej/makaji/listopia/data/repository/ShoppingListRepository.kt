package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.di.annotation.Local
import com.aleksej.makaji.listopia.di.annotation.Remote
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListRepository @Inject constructor(@Remote private val mRemoteShoppingListDataSource: ShoppingListDataSource,
                                                 @Local private val mLocalShoppingListDataSource: ShoppingListDataSource): ShoppingListDataSource {

    override fun getShoppingLists(): LiveData<StateHandler<PagedList<ShoppingListModel>>> {
        return mLocalShoppingListDataSource.getShoppingLists()
    }

    override fun getShoppingList(): LiveData<StateHandler<ShoppingListModel>> {
        return mLocalShoppingListDataSource.getShoppingList()
    }
}