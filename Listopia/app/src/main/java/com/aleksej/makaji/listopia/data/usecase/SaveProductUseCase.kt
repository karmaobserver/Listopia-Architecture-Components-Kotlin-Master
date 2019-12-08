package com.aleksej.makaji.listopia.data.usecase

import android.content.Context
import android.util.Log
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import com.aleksej.makaji.listopia.error.UnknownError
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.aleksej.makaji.listopia.util.Validator
import com.aleksej.makaji.listopia.util.isConnectedToNetwork
import com.aleksej.makaji.listopia.worker.WorkerUtil
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class SaveProductUseCase @Inject constructor(private val mProductRepository: ProductRepository,
                                             private val mSharedPreferenceManager: SharedPreferenceManager,
                                             private val mContext: Context): UseCase<SaveProductValue, Long>() {

    override suspend fun invoke(value: SaveProductValue): State<Long> {
        Validator.validateProductName(value.name)?.run {
            return ErrorState(this)
        }
        var returnValue : State<Long> = ErrorState(UnknownError)
        when (val saveProductRoom = mProductRepository.saveProduct(value)) {
            is SuccessState -> {
                if (mSharedPreferenceManager.userId.isBlank()) return saveProductRoom
                when (val getProductRoom = mProductRepository.getProductByIdSuspend(value.id)) {
                    is SuccessState -> {
                        getProductRoom.data?.let {
                            if (!mContext.isConnectedToNetwork()) {
                                WorkerUtil.createProductSynchronizeWorker(mContext)
                                return SuccessState(1L)
                            } else {
                                when (val saveProductRemote = mProductRepository.saveProductRemote(it)) {
                                    is SuccessState -> {
                                        mProductRepository.updateSyncProduct(it.id)
                                        return saveProductRoom
                                    }
                                    is LoadingState -> return LoadingState()
                                    is ErrorState -> { return ErrorState(saveProductRemote.error) }
                                }
                            }
                        }
                    }
                    is LoadingState -> return LoadingState()
                    is ErrorState -> return ErrorState(getProductRoom.error)
                }
            }
            else -> returnValue = saveProductRoom
        }
        return returnValue
    }
}