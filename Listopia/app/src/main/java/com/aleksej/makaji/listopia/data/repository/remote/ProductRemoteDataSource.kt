package com.aleksej.makaji.listopia.data.repository.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ProductDataSource
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
@Singleton
class ProductRemoteDataSource @Inject constructor() : ProductDataSource {

    override fun getProductsByShoppingListId(productsValue: ProductsValue): LiveData<StateHandler<PagedList<ProductModel>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveProduct(saveProductValue: SaveProductValue): Deferred<State<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteProductsByShoppingList(deleteProductValue: DeleteProductValue): Deferred<State<Int>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}