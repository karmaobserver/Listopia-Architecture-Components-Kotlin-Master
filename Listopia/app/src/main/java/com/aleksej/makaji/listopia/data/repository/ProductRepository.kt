package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
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

    override fun getProductsByShoppingListId(productsValue: ProductsValue): LiveData<StateHandler<PagedList<ProductModel>>> {
        return mLocalProductDataSource.getProductsByShoppingListId(productsValue)
    }

    override suspend fun saveProduct(saveProductValue: SaveProductValue): State<Long> {
        return mLocalProductDataSource.saveProduct(saveProductValue)
    }

    override suspend fun deleteProductsByShoppingList(deleteProductValue: DeleteProductValue): State<Int> {
        return mLocalProductDataSource.deleteProductsByShoppingList(deleteProductValue)
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