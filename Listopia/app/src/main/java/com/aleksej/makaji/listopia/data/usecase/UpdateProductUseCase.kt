package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.util.Validator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2/3/19.
 */
class UpdateProductUseCase @Inject constructor(private val mProductRepository: ProductRepository) : UseCase<ProductModel, LiveData<StateHandler<Int>>> {

    private val mUseCaseLiveData = MutableLiveData<StateHandler<Int>>()

    override fun invoke(value: ProductModel): LiveData<StateHandler<Int>> {
        Validator.validateProductName(value.name)?.let {
            mUseCaseLiveData.postValue(StateHandler.error(it))
            return mUseCaseLiveData
        }
        GlobalScope.launch {
            mUseCaseLiveData.postValue(StateHandler.loading())
            val updateProductResponse = mProductRepository.updateProduct(value)
            mUseCaseLiveData.postValue(StateHandler(updateProductResponse))
        }
        return mUseCaseLiveData
    }
}