package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class SaveProductUseCase @Inject constructor(private val mProductRepository: ProductRepository): UseCase<SaveProductValue, LiveData<StateHandler<Long>>> {

    private val saveProductLiveData = MutableLiveData<StateHandler<Long>>()

    override fun invoke(value: SaveProductValue): LiveData<StateHandler<Long>> {
        GlobalScope.launch {
            saveProductLiveData.postValue(StateHandler.loading())
            val saveProductResponse = mProductRepository.saveProduct(value).await()
            saveProductLiveData.postValue(StateHandler(saveProductResponse))
        }
        return saveProductLiveData
    }
}