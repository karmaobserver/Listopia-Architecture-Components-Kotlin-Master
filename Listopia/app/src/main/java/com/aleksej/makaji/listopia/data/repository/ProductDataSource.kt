package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.*
import kotlinx.coroutines.Deferred

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
interface ProductDataSource {
    fun getProductsByShoppingListId(productsValue: ProductsValue) : LiveData<StateHandler<PagedList<ProductModel>>>
    fun getProductById(productValue: ProductValue) : LiveData<StateHandler<ProductModel>>
    suspend fun saveProduct(saveProductValue: SaveProductValue): Deferred<State<Long>>
    suspend fun updateProduct(productModel: ProductModel): Deferred<State<Int>>
    suspend fun deleteProductsByShoppingList(deleteProductValue: DeleteProductValue): Deferred<State<Int>>
    suspend fun deleteProductById(productValue: ProductValue): Deferred<State<Int>>
}