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
import com.aleksej.makaji.listopia.data.mapper.mapToShoppingListModel
import com.aleksej.makaji.listopia.data.repository.ShoppingListDataSource
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.room.ShoppingListDao
import com.aleksej.makaji.listopia.data.usecase.value.*
import com.aleksej.makaji.listopia.error.RoomDeleteError
import com.aleksej.makaji.listopia.error.RoomError
import com.aleksej.makaji.listopia.error.RoomUpdateError
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
        //Just for Testing to fill data first time
        /*async {
            mShoppingListDao.insertShoppingList(ShoppingList(1, "Name-1", "123"))
            mShoppingListDao.insertShoppingList(ShoppingList(2, "Name-2", "312"))
            mShoppingListDao.insertShoppingList(ShoppingList(3, "Name-3", "333"))
            mShoppingListDao.insertShoppingList(ShoppingList(4, "Name-4", "444"))
        }*/
        shoppingListsLiveData.postValue(StateHandler.loading())

        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(PAGED_LIST_ENABLE_PLACEHOLDERS)
                .setPageSize(PAGED_LIST_PAGE_SIZE)
                .build()

        val livePagedListOrder = LivePagedListBuilder(mShoppingListDao.getShoppingLists().map {
            it.mapToShoppingListModel()
        }, pagedListConfig)
                .build()

        return Transformations.switchMap(livePagedListOrder) {
            shoppingListsLiveData.postValue(StateHandler.success(it))
            return@switchMap shoppingListsLiveData
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
            shoppingListLiveData.postValue(StateHandler.error(RoomError))
        }
        return shoppingListLiveData
    }

    override suspend fun saveShoppingList(saveShoppingListValue: SaveShoppingListValue): State<Long> {
        return try {
            SuccessState(mShoppingListDao.saveShoppingList(saveShoppingListValue.mapToShoppingList()))
        }catch (e: Exception){
            ErrorState(RoomError)
        }
    }

    override suspend fun deleteAllShoppingLists(): State<Int> {
        return try {
            SuccessState(mShoppingListDao.deleteAllShoppingLists())
        }catch (e: Exception){
            ErrorState(RoomDeleteError)
        }
    }

    override suspend fun updateShoppingList(shoppingListValue: ShoppingListValue): State<Int> {
        return try {
            SuccessState(mShoppingListDao.updateShoppingList(shoppingListValue.mapToShoppingList()))
        }catch (e: Exception){
            ErrorState(RoomUpdateError)
        }
    }

    override suspend fun deleteShoppingListById(deleteShoppingListValue: DeleteShoppingListValue): State<Int> {
        return try {
            SuccessState(mShoppingListDao.deleteShoppingListById(deleteShoppingListValue.id))
        }catch (e: Exception){
            ErrorState(RoomDeleteError)
        }
    }

    override suspend fun fetchShoppingLists(): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun fetchShoppingListsByUserId(fetchShoppingListsValue: FetchShoppingListsValue): State<List<ShoppingListModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}