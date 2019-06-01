package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.util.Validator
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2/3/19.
 */
class UpdateProductUseCase @Inject constructor(private val mProductRepository: ProductRepository) : UseCase<ProductModel, Int>() {

    override suspend fun invoke(value: ProductModel): State<Int> {
        Validator.validateProductName(value.name)?.run {
            return ErrorState(this)
        }
        return mProductRepository.updateProduct(value)
    }
}