package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.usecase.value.ProductValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/27/19.
 */
class DeleteProductByIdUseCase @Inject constructor(private val mProductRepository: ProductRepository) : UseCase<ProductValue, Int>() {

    override suspend fun invoke(value: ProductValue): State<Int> {
        return mProductRepository.deleteProductById(value)
    }
}