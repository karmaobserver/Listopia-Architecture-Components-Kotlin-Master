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
import com.aleksej.makaji.listopia.data.usecase.value.*
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

    override suspend fun deleteProductsByShoppingList(deleteProductsValue: DeleteProductsValue): State<Int> {
        return try {
            SuccessState(mProductDao.deleteProductsByShoppingList(deleteProductsValue.shoppingListId))
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

    override suspend fun getProductsSuspend(): State<List<ProductModel>> {
        return try {
            SuccessState(mProductDao.getProductsSuspend().map {
                it.mapToProductModel()
            })
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveProducts(products: List<ProductModel>): State<List<Long>> {
        return try {
            SuccessState(mProductDao.saveProducts(products.map {
                it.mapToProduct()
            }))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun getProductByIdSuspend(productId: String): State<ProductModel> {
        return try {
            val result = mProductDao.getProductByIdSuspend(productId)
            if (result == null) {
                SuccessState(null)
            } else {
                SuccessState(result.mapToProductModel())
            }
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun getProductsNotSyncedSuspend(): State<List<ProductModel>> {
        return try {
            SuccessState(mProductDao.getProductsNotSyncedSuspend().map {
                it.mapToProductModel()
            })
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun updateSyncProduct(productId: String): State<Int> {
        return try {
            SuccessState(mProductDao.updateSyncProduct(productId))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun updateSyncProducts(productIds: List<String>): State<Int> {
        return try {
            SuccessState(mProductDao.updateSyncProducts(productIds))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveOrUpdateProductsRemote(productModels: List<ProductModel>): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun fetchProducts(shoppingListsId: List<String>): State<List<ProductModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveProductRemote(productModel: ProductModel): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateProductRemote(productModel: ProductModel): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteProductByIdRemote(deleteProductValue: DeleteProductValue): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun fetchProductById(fetchAndSaveProductValue: FetchAndSaveProductValue): State<ProductModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}