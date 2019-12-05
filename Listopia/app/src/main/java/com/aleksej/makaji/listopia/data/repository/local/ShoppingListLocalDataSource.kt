package com.aleksej.makaji.listopia.data.repository.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.mapper.mapToShoppingList
import com.aleksej.makaji.listopia.data.mapper.mapToShoppingListEditor
import com.aleksej.makaji.listopia.data.mapper.mapToShoppingListModel
import com.aleksej.makaji.listopia.data.mapper.mapToShoppingListModelAsSynced
import com.aleksej.makaji.listopia.data.repository.ShoppingListDataSource
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.room.dao.ShoppingListDao
import com.aleksej.makaji.listopia.data.usecase.value.*
import com.aleksej.makaji.listopia.error.RoomError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Singleton
class ShoppingListLocalDataSource @Inject constructor(private val mShoppingListDao: ShoppingListDao) : ShoppingListDataSource, CoroutineScope {

    companion object {
        private const val PAGED_LIST_PAGE_SIZE = 12
        private const val PAGED_LIST_ENABLE_PLACEHOLDERS = true
    }

    private val shoppingListsLiveData = MutableLiveData<StateHandler<PagedList<ShoppingListModel>>>()

    private val shoppingListLiveData = MutableLiveData<StateHandler<ShoppingListModel>>()

    override val coroutineContext: CoroutineContext
        get() = Job()

    override fun getShoppingLists(): LiveData<StateHandler<PagedList<ShoppingListModel>>> {
        shoppingListsLiveData.postValue(StateHandler.loading())

        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(PAGED_LIST_ENABLE_PLACEHOLDERS)
                .setPageSize(PAGED_LIST_PAGE_SIZE)
                .build()

        val livePagedListOrder = LivePagedListBuilder(mShoppingListDao.getShoppingListsWithEditors().map {
            it.mapToShoppingListModel()
        }, pagedListConfig)
                .build()

        return Transformations.switchMap(livePagedListOrder) {
            shoppingListsLiveData.postValue(StateHandler.success(it))
            return@switchMap shoppingListsLiveData
        }
    }

    override suspend fun getShoppingListsSuspend(): State<List<ShoppingListModel>> {
        return try {
            SuccessState(mShoppingListDao.getShoppingListsSuspend().map {
                it.mapToShoppingListModel()
            })
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override fun getShoppingListById(shoppingListByIdValue: ShoppingListByIdValue): LiveData<StateHandler<ShoppingListModel>> {
        shoppingListLiveData.postValue(StateHandler.loading())
        try {
            return Transformations.switchMap(mShoppingListDao.getShoppingListById(shoppingListByIdValue.id)) {
                it?.run {
                    shoppingListLiveData.postValue(StateHandler.success(it.mapToShoppingListModel()))
                }
                return@switchMap shoppingListLiveData
            }
        } catch (e: Exception) {
            shoppingListLiveData.postValue(StateHandler.error(RoomError(e)))
        }
        return shoppingListLiveData
    }

    override suspend fun saveShoppingList(saveShoppingListValue: SaveShoppingListValue): State<Long> {
        return try {
            SuccessState(mShoppingListDao.saveShoppingList(saveShoppingListValue.mapToShoppingList()))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveShoppingLists(shoppingLists: List<ShoppingListModel>): State<List<Long>> {
        return try {
            SuccessState(mShoppingListDao.saveShoppingLists(shoppingLists.map {
                it.mapToShoppingList()
            }))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveShoppingListsWithEditors(saveShoppingListEditorValue: List<SaveShoppingListEditorValue>): State<List<Long>> {
        return try {
            SuccessState(mShoppingListDao.saveShoppingListsWithEditors(saveShoppingListEditorValue.map {
                it.mapToShoppingListEditor()
            }))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveShoppingListRemote(shoppingListModel: ShoppingListModel): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAllShoppingLists(): State<Int> {
        return try {
            SuccessState(mShoppingListDao.deleteAllShoppingLists())
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun updateShoppingList(shoppingListModel: ShoppingListModel): State<Int> {
        return try {
            SuccessState(mShoppingListDao.updateShoppingList(shoppingListModel.mapToShoppingList()))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue): State<Int> {
        return try {
            SuccessState(mShoppingListDao.deleteShoppingListById(deleteShoppingListValue.id))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun updateSyncShoppingList(shoppingListId: String): State<Int> {
        return try {
            SuccessState(mShoppingListDao.updateSyncShoppingList(shoppingListId))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }


    override suspend fun getShoppingListByIdSuspend(shoppingListByIdValue: ShoppingListByIdValue): State<ShoppingListModel?> {
        return try {
            val result = mShoppingListDao.getShoppingListByIdSuspend(shoppingListByIdValue.id)
            if (result == null) {
                SuccessState(null)
            } else {
                SuccessState(result.mapToShoppingListModelAsSynced())
            }
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun deleteShoppingListsWithEditorsById(deleteShoppingListValue: DeleteShoppingListValue): State<Int> {
        return try {
            SuccessState(mShoppingListDao.deleteShoppingListsWithEditorsById(deleteShoppingListValue.id))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun updateShoppingListRemote(shoppingListModel: ShoppingListModel): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun fetchShoppingListsByUserId(fetchShoppingListsValue: FetchShoppingListsValue): State<List<ShoppingListModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteShoppingListByIdRemote(shoppingListId: String): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun fetchShoppingListById(shoppingListId: String): State<ShoppingListModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}