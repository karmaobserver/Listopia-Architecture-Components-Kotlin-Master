package com.aleksej.makaji.listopia.data.repository.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.api.ListopiaApi
import com.aleksej.makaji.listopia.data.api.callback.CoroutineAdapter
import com.aleksej.makaji.listopia.data.api.dto.request.FetchProductsRequest
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.mapper.mapToSaveProductRequest
import com.aleksej.makaji.listopia.data.mapper.mapToUpdateProductRequest
import com.aleksej.makaji.listopia.data.repository.ProductDataSource
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.*
import com.aleksej.makaji.listopia.error.ExceptionError
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
@Singleton
class ProductRemoteDataSource @Inject constructor(private val mListopiaApi: ListopiaApi, private val mRetrofit: Retrofit) : ProductDataSource {
    override suspend fun getProductByIdSuspend(productId: String): State<ProductModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveProducts(products: List<ProductModel>): State<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProductsSuspend(): State<List<ProductModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun fetchProducts(shoppingListsId: List<String>): State<List<ProductModel>> {
        return try {
            CoroutineAdapter(mListopiaApi.fetchProducts(FetchProductsRequest(shoppingListsId)), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun saveProductRemote(productModel: ProductModel): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.saveProduct(productModel.mapToSaveProductRequest()), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun updateProductRemote(productModel: ProductModel): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.updateProduct(productModel.mapToUpdateProductRequest()), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun deleteProductByIdRemote(deleteProductValue: DeleteProductValue): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.deleteProductById(deleteProductValue.productId, deleteProductValue.shoppingListId), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun updateSyncProduct(productId: String): State<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProductById(productValue: ProductValue): LiveData<StateHandler<ProductModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProductsByShoppingListId(productsValue: ProductsValue): LiveData<StateHandler<PagedList<ProductModel>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveProduct(saveProductValue: SaveProductValue): State<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteProductsByShoppingList(deleteProductsValue: DeleteProductsValue): State<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateProduct(productModel: ProductModel): State<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteProductById(productValue: ProductValue): State<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}