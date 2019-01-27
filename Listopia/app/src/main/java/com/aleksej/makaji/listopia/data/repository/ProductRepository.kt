package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import com.aleksej.makaji.listopia.di.annotation.Local
import com.aleksej.makaji.listopia.di.annotation.Remote
import kotlinx.coroutines.Deferred
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class ProductRepository @Inject constructor(@Remote private val mRemoteProductDataSource: ProductDataSource,
                                                 @Local private val mLocalProductDataSource: ProductDataSource): ProductDataSource {

    override fun getProductsByShoppingListId(productsValue: ProductsValue): LiveData<StateHandler<PagedList<ProductModel>>> {
        return mLocalProductDataSource.getProductsByShoppingListId(productsValue)
    }

    override suspend fun saveProduct(saveProductValue: SaveProductValue): Deferred<State<Long>> {
        return mLocalProductDataSource.saveProduct(saveProductValue)
    }

    override suspend fun deleteProductsByShoppingList(deleteProductValue: DeleteProductValue): Deferred<State<Int>> {
        return mLocalProductDataSource.deleteProductsByShoppingList(deleteProductValue)
    }
}