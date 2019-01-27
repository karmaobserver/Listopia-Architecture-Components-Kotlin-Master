package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class GetProductsByShoppingListUseCase @Inject constructor(private val mProductRepository: ProductRepository): UseCase<ProductsValue, LiveData<StateHandler<PagedList<ProductModel>>>> {

    override fun invoke(value: ProductsValue): LiveData<StateHandler<PagedList<ProductModel>>> {
        return mProductRepository.getProductsByShoppingListId(value)
    }
}