package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.usecase.value.DeleteProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/27/19.
 */
class DeleteProductByIdUseCase @Inject constructor(private val mProductRepository: ProductRepository) : UseCase<DeleteProductValue, Int>() {

    override suspend fun invoke(value: DeleteProductValue): State<Int> {
        return when (val deletedProductRoom = mProductRepository.deleteProductById(ProductValue(value.productId))) {
            is SuccessState -> {
                when (val deleteProductRemote = mProductRepository.deleteProductByIdRemote(value)) {
                    is SuccessState -> deletedProductRoom
                    is LoadingState -> LoadingState()
                    is ErrorState -> ErrorState(deleteProductRemote.error)
                }
            }
            else -> deletedProductRoom
        }
    }
}