package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.ProductValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/27/19.
 */
class GetProductByIdUseCase @Inject constructor(private val mProductRepository: ProductRepository): UseCase<ProductValue, LiveData<StateHandler<ProductModel>>> {

    override fun invoke(value: ProductValue): LiveData<StateHandler<ProductModel>> {
        return mProductRepository.getProductById(value)
    }
}