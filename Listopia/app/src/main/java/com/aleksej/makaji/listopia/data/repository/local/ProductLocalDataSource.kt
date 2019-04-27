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
import com.aleksej.makaji.listopia.data.repository.ProductDataSource
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.room.ProductDao
import com.aleksej.makaji.listopia.data.usecase.value.DeleteProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import com.aleksej.makaji.listopia.error.RoomDeleteError
import com.aleksej.makaji.listopia.error.RoomError
import com.aleksej.makaji.listopia.error.RoomUpdateError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
@Singleton
class ProductLocalDataSource @Inject constructor(private val mProductDao: ProductDao): ProductDataSource, CoroutineScope {

    companion object {
        private const val PAGED_LIST_PAGE_SIZE = 12
        private const val PAGED_LIST_ENABLE_PLACEHOLDERS = true
    }

    private val productsLiveData = MutableLiveData<StateHandler<PagedList<ProductModel>>>()

    private val productLiveData = MutableLiveData<StateHandler<ProductModel>>()

    override val coroutineContext: CoroutineContext
        get() = Job()

    override fun getProductsByShoppingListId(productsValue: ProductsValue): LiveData<StateHandler<PagedList<ProductModel>>> {
        productsLiveData.postValue(StateHandler.loading())

        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(PAGED_LIST_ENABLE_PLACEHOLDERS)
                .setPageSize(PAGED_LIST_PAGE_SIZE)
                .build()

        val livePagedListOrder = LivePagedListBuilder(mProductDao.getProductsByShoppingListId(productsValue.shoppingListId).map {
            RoomToModelMapper.mapProduct(it)
        }, pagedListConfig)
                .build()

        return Transformations.switchMap(livePagedListOrder) {
            productsLiveData.postValue(StateHandler.success(it))
            return@switchMap productsLiveData
        }
    }

    override suspend fun saveProduct(saveProductValue: SaveProductValue): Deferred<State<Long>> {
        return async {
            try {
                State.Success(mProductDao.saveProduct(ValueToRoomMapper.mapProduct(saveProductValue)))
            } catch (e: Exception){
                State.Error<Long>(RoomError)
            }
        }
    }

    override suspend fun deleteProductsByShoppingList(deleteProductValue: DeleteProductValue): Deferred<State<Int>> {
        return async {
            try {
                State.Success(mProductDao.deleteProductsByShoppingList(deleteProductValue.shoppingListId))
            } catch (e: Exception){
                State.Error<Int>(RoomDeleteError)
            }
        }
    }

    override suspend fun updateProduct(productModel: ProductModel): Deferred<State<Int>> {
        return async {
            try {
                State.Success(mProductDao.updateProduct(ModelToRoomMapper.mapProduct(productModel)))
            } catch (e: Exception){
                State.Error<Int>(RoomUpdateError)
            }
        }
    }

    override fun getProductById(productValue: ProductValue): LiveData<StateHandler<ProductModel>> {
        productLiveData.postValue(StateHandler.loading())
        try {
            return Transformations.switchMap(mProductDao.getProductById(productValue.productId)) {
                it?.run {
                    productLiveData.postValue(StateHandler.success(RoomToModelMapper.mapProduct(it)))
                }
                return@switchMap productLiveData
            }
        } catch (e: Exception) {
            productLiveData.postValue(StateHandler.error(RoomError))
        }
        return productLiveData
    }

    override suspend fun deleteProductById(productValue: ProductValue): Deferred<State<Int>> {
        return async {
            try {
                State.Success(mProductDao.deleteProductById(productValue.productId))
            } catch (e: Exception){
                State.Error<Int>(RoomDeleteError)
            }
        }
    }
}