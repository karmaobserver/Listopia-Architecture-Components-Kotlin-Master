package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import com.aleksej.makaji.listopia.util.Validator
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class SaveProductUseCase @Inject constructor(private val mProductRepository: ProductRepository): UseCase<SaveProductValue, Long>() {

    override suspend fun invoke(value: SaveProductValue): State<Long> {
        Validator.validateProductName(value.name)?.run {
            return ErrorState(this)
        }
        return mProductRepository.saveProduct(value)
    }
}