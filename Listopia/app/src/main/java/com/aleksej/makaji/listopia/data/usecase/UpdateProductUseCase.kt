package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.error.UnknownError
import com.aleksej.makaji.listopia.util.Validator
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2/3/19.
 */
class UpdateProductUseCase @Inject constructor(private val mProductRepository: ProductRepository) : UseCase<ProductModel, Int>() {

    override suspend fun invoke(value: ProductModel): State<Int> {
        Validator.validateProductName(value.name)?.run {
            return ErrorState(this)
        }
        var returnValue : State<Int> = ErrorState(UnknownError)
        value.timestamp = Date()
        when (val updateProductRoom = mProductRepository.updateProduct(value)) {
            is SuccessState -> {
                value.isSynced = false
                when (val updateProductRemote = mProductRepository.updateProductRemote(value)) {
                    is SuccessState -> {
                        mProductRepository.updateSyncProduct(value.id)
                        return updateProductRoom
                    }
                    is LoadingState -> return LoadingState()
                    is ErrorState -> {
                        Timber.d( "error: updateShoppingListUseCase")
                        return ErrorState(updateProductRemote.error)
                    }
                }
            }
            else -> returnValue = updateProductRoom
        }
        return returnValue
    }
}