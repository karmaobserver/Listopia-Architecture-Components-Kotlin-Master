package com.aleksej.makaji.listopia.data.repository.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.api.ListopiaApi
import com.aleksej.makaji.listopia.data.api.callback.CoroutineAdapter
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.mapper.mapToSaveShoppingListRequest
import com.aleksej.makaji.listopia.data.mapper.mapToUpdateShoppingListRequest
import com.aleksej.makaji.listopia.data.repository.ShoppingListDataSource
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.*
import com.aleksej.makaji.listopia.error.ExceptionError
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Singleton
class ShoppingListRemoteDataSource @Inject constructor(private val mListopiaApi: ListopiaApi, private val mRetrofit: Retrofit) : ShoppingListDataSource {

    override suspend fun saveShoppingLists(shoppingLists: List<ShoppingListModel>): State<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveShoppingListsWithEditors(saveShoppingListEditorValue: List<SaveShoppingListEditorValue>): State<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getShoppingListsSuspend(): State<List<ShoppingListModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getShoppingListByIdSuspend(shoppingListByIdValue: ShoppingListByIdValue): State<ShoppingListModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateSyncShoppingList(shoppingListId: String): State<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShoppingListById(shoppingListByIdValue: ShoppingListByIdValue): LiveData<StateHandler<ShoppingListModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShoppingLists(): LiveData<StateHandler<PagedList<ShoppingListModel>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveShoppingList(saveShoppingListValue: SaveShoppingListValue): State<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAllShoppingLists(): State<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateShoppingList(shoppingListModel: ShoppingListModel): State<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue): State<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveShoppingListRemote(shoppingListModel: ShoppingListModel): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.saveShoppingList(shoppingListModel.mapToSaveShoppingListRequest()), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun fetchShoppingListsByUserId(fetchShoppingListsValue: FetchShoppingListsValue): State<List<ShoppingListModel>> {
        return try {
            CoroutineAdapter(mListopiaApi.fetchShoppingListsByUserId(fetchShoppingListsValue.userId), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun updateShoppingListRemote(shoppingListModel: ShoppingListModel): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.updateShoppingList(shoppingListModel.mapToUpdateShoppingListRequest()), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun deleteShoppingListByIdRemote(shoppingListId: String): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.deleteShoppingListById(shoppingListId), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

}