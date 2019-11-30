package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.*

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
interface ProductDataSource {
    fun getProductsByShoppingListId(productsValue: ProductsValue) : LiveData<StateHandler<PagedList<ProductModel>>>
    fun getProductById(productValue: ProductValue) : LiveData<StateHandler<ProductModel>>
    suspend fun getProductByIdSuspend(productId: String) : State<ProductModel>
    suspend fun getProductsSuspend(): State<List<ProductModel>>
    suspend fun saveProduct(saveProductValue: SaveProductValue): State<Long>
    suspend fun saveProducts(products: List<ProductModel>): State<List<Long>>
    suspend fun updateProduct(productModel: ProductModel): State<Int>
    suspend fun deleteProductsByShoppingList(deleteProductsValue: DeleteProductsValue): State<Int>
    suspend fun deleteProductById(productValue: ProductValue): State<Int>
    suspend fun updateSyncProduct(productId: String): State<Int>
    suspend fun fetchProducts(shoppingListsId: List<String>): State<List<ProductModel>>
    suspend fun saveProductRemote(productModel: ProductModel): State<Unit>
    suspend fun updateProductRemote(productModel: ProductModel): State<Unit>
    suspend fun deleteProductByIdRemote(deleteProductValue: DeleteProductValue): State<Unit>
}