package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.usecase.value.ProductValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/27/19.
 */
class DeleteProductByIdUseCase @Inject constructor(private val mProductRepository: ProductRepository) : UseCase<ProductValue, LiveData<StateHandler<Int>>> {

    private val useCaseLiveData = MutableLiveData<StateHandler<Int>>()

    override fun invoke(value: ProductValue): LiveData<StateHandler<Int>> {
        GlobalScope.launch {
            useCaseLiveData.postValue(StateHandler.loading())
            val deleteProductResponse = mProductRepository.deleteProductById(value).await()
            useCaseLiveData.postValue(StateHandler(deleteProductResponse))
        }
        return useCaseLiveData
    }
}