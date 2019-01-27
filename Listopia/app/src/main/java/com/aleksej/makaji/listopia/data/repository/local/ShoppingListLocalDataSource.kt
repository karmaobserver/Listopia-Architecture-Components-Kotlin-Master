package com.aleksej.makaji.listopia.data.repository.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.mapper.ModelToRoomMapper
import com.aleksej.makaji.listopia.data.mapper.RoomToModelMapper
import com.aleksej.makaji.listopia.data.mapper.ValueToRoomMapper
import com.aleksej.makaji.listopia.data.repository.ShoppingListDataSource
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.room.ShoppingListDao
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import com.aleksej.makaji.listopia.error.RoomDeletingError
import com.aleksej.makaji.listopia.error.RoomError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
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
            RoomToModelMapper.mapShoppingList(it)
        }, pagedListConfig)
                .build()

        return Transformations.switchMap(livePagedListOrder) {
            shoppingListsLiveData.postValue(StateHandler.success(it))
            return@switchMap shoppingListsLiveData
        }
    }

    override fun getShoppingList(): LiveData<StateHandler<ShoppingListModel>> {
        shoppingListLiveData.postValue(StateHandler.loading())
        try {
            return Transformations.switchMap(mShoppingListDao.getShoppingList()) {
                it?.run {
                    shoppingListLiveData.postValue(StateHandler.success(RoomToModelMapper.mapShoppingList(it)))
                }
                return@switchMap shoppingListLiveData
            }
        } catch (e: Exception) {
            shoppingListLiveData.postValue(StateHandler.error(RoomError))
        }
        return shoppingListLiveData
    }

    override suspend fun saveShoppingList(shoppingListValue: ShoppingListValue): Deferred<State<Long>> {
        return async {
            try {
                State.Success(mShoppingListDao.saveShoppingList(ValueToRoomMapper.mapShoppingList(shoppingListValue)))
            } catch (e: Exception){
                State.Error<Long>(RoomError)
            }
        }
    }

    override suspend fun deleteAllShoppingLists(): Deferred<State<Int>> {
        return async {
            try {
                State.Success(mShoppingListDao.deleteAllShoppingLists())
            } catch (e: Exception){
                State.Error<Int>(RoomDeletingError)
            }
        }
    }
}