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
import com.aleksej.makaji.listopia.data.mapper.*
import com.aleksej.makaji.listopia.data.repository.ProductDataSource
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.room.dao.ProductDao
import com.aleksej.makaji.listopia.data.usecase.value.DeleteProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import com.aleksej.makaji.listopia.error.RoomError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
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

        val livePagedListOrder = LivePagedListBuilder<Int,ProductModel>(mProductDao.getProductsByShoppingListId(productsValue.shoppingListId).map {
            it.mapToProductModel()
        }, pagedListConfig)
                .build()

        return Transformations.switchMap(livePagedListOrder) {
            productsLiveData.postValue(StateHandler.success(it))
            return@switchMap productsLiveData
        }
    }

    override suspend fun saveProduct(saveProductValue: SaveProductValue): State<Long> {
        return try {
            SuccessState(mProductDao.saveProduct(saveProductValue.mapToProduct()))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun deleteProductsByShoppingList(deleteProductValue: DeleteProductValue): State<Int> {
        return try {
            SuccessState(mProductDao.deleteProductsByShoppingList(deleteProductValue.shoppingListId))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun updateProduct(productModel: ProductModel): State<Int> {
        return try {
            SuccessState(mProductDao.updateProduct(productModel.mapToProduct()))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override fun getProductById(productValue: ProductValue): LiveData<StateHandler<ProductModel>> {
        productLiveData.postValue(StateHandler.loading())
        try {
            return Transformations.switchMap(mProductDao.getProductById(productValue.productId)) {
                it?.run {
                    productLiveData.postValue(StateHandler.success(it.mapToProductModel()))
                }
                return@switchMap productLiveData
            }
        } catch (e: Exception) {
            productLiveData.postValue(StateHandler.error(RoomError(e)))
        }
        return productLiveData
    }

    override suspend fun deleteProductById(productValue: ProductValue): State<Int> {
        return try {
            SuccessState(mProductDao.deleteProductById(productValue.productId))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }
}