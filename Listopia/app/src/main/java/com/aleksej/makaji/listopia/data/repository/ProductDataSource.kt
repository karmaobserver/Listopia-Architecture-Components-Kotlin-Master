package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import kotlinx.coroutines.Deferred

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
interface ProductDataSource {
    fun getProductsByShoppingListId(productsValue: ProductsValue) : LiveData<StateHandler<PagedList<ProductModel>>>
    suspend fun saveProduct(saveProductValue: SaveProductValue): Deferred<State<Long>>
    suspend fun deleteProductsByShoppingList(deleteProductValue: DeleteProductValue): Deferred<State<Int>>
}