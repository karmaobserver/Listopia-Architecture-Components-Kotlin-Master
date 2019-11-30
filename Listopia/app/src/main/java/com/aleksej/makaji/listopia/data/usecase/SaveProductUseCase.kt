package com.aleksej.makaji.listopia.data.usecase

import android.util.Log
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import com.aleksej.makaji.listopia.error.UnknownError
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
        var returnValue : State<Long> = ErrorState(UnknownError)
        when (val saveProductRoom = mProductRepository.saveProduct(value)) {
            is SuccessState -> {
                when (val getProductRoom = mProductRepository.getProductByIdSuspend(value.id)) {
                    is SuccessState -> {
                        getProductRoom.data?.let {
                            when (mProductRepository.saveProductRemote(it)) {
                                is SuccessState -> {
                                    mProductRepository.updateSyncProduct(it.id)
                                    return saveProductRoom
                                }
                                else -> {
                                    returnValue =  saveProductRoom
                                    Log.d("SaveShoppingListUseCase", "error: saveShoppingListRemote")
                                }
                            }
                        }
                    }
                    else -> {
                        returnValue = saveProductRoom
                        Log.d("SaveShoppingListUseCase", "error: getShoppingListRoom")
                    }
                }
            }
            else -> returnValue = saveProductRoom
        }
        return returnValue
    }
}