package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.*
import com.aleksej.makaji.listopia.di.annotation.Local
import com.aleksej.makaji.listopia.di.annotation.Remote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
@Singleton
class ProductRepository @Inject constructor(@Remote private val mRemoteProductDataSource: ProductDataSource,
                                                 @Local private val mLocalProductDataSource: ProductDataSource): ProductDataSource {
    override suspend fun fetchProductById(fetchAndSaveProductValue: FetchAndSaveProductValue): State<ProductModel> {
        return mRemoteProductDataSource.fetchProductById(fetchAndSaveProductValue)
    }

    override suspend fun getProductByIdSuspend(productId: String): State<ProductModel> {
        return mLocalProductDataSource.getProductByIdSuspend(productId)
    }

    override suspend fun saveProducts(products: List<ProductModel>): State<List<Long>> {
        return mLocalProductDataSource.saveProducts(products)
    }

    override suspend fun getProductsSuspend(): State<List<ProductModel>> {
        return mLocalProductDataSource.getProductsSuspend()
    }

    override suspend fun updateSyncProduct(productId: String): State<Int> {
        return mLocalProductDataSource.updateSyncProduct(productId)
    }

    override suspend fun fetchProducts(shoppingListsId: List<String>): State<List<ProductModel>> {
        return mRemoteProductDataSource.fetchProducts(shoppingListsId)
    }

    override suspend fun saveProductRemote(productModel: ProductModel): State<Unit> {
        return mRemoteProductDataSource.saveProductRemote(productModel)
    }

    override suspend fun updateProductRemote(productModel: ProductModel): State<Unit> {
        return mRemoteProductDataSource.updateProductRemote(productModel)
    }

    override suspend fun deleteProductByIdRemote(deleteProductValue: DeleteProductValue): State<Unit> {
        return mRemoteProductDataSource.deleteProductByIdRemote(deleteProductValue)
    }

    override fun getProductsByShoppingListId(productsValue: ProductsValue): LiveData<StateHandler<PagedList<ProductModel>>> {
        return mLocalProductDataSource.getProductsByShoppingListId(productsValue)
    }

    override suspend fun saveProduct(saveProductValue: SaveProductValue): State<Long> {
        return mLocalProductDataSource.saveProduct(saveProductValue)
    }

    override suspend fun deleteProductsByShoppingList(deleteProductsValue: DeleteProductsValue): State<Int> {
        return mLocalProductDataSource.deleteProductsByShoppingList(deleteProductsValue)
    }

    override suspend fun updateProduct(productModel: ProductModel): State<Int> {
        return mLocalProductDataSource.updateProduct(productModel)
    }

    override fun getProductById(productValue: ProductValue): LiveData<StateHandler<ProductModel>> {
        return mLocalProductDataSource.getProductById(productValue)
    }

    override suspend fun deleteProductById(productValue: ProductValue): State<Int> {
        return mLocalProductDataSource.deleteProductById(productValue)
    }
}