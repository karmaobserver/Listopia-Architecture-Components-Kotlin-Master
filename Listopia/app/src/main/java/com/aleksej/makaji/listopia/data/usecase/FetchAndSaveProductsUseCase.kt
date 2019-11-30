package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.FetchProductsValue
import com.aleksej.makaji.listopia.error.UnknownError
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-11-30.
 */
class FetchAndSaveProductsUseCase @Inject constructor(private val mProductRepository: ProductRepository) : UseCase<FetchProductsValue, Unit>() {

    override suspend fun invoke(value: FetchProductsValue): State<Unit> {
        when (val fetchedProducts = mProductRepository.fetchProducts(value.shoppingListsId)) {
            is SuccessState -> {
                fetchedProducts.data?.let { remoteProducts ->
                    when (val productsRoom = mProductRepository.getProductsSuspend()) {
                        is SuccessState -> {
                            val roomProducts = productsRoom.data
                            val productsToBeAdded = arrayListOf<ProductModel>()
                            remoteProducts.forEach {
                                val index = roomProducts?.indexOfFirst { roomProductModel ->
                                    roomProductModel.id == it.id
                                } // -1 if not found
                                if (index != null && index >= 0) {
                                    //If shopping list exists in database
                                    //TODO Add additional rules
                                    val roomProductModel = roomProducts[index]
                                    if (it.timestamp > roomProductModel.timestamp) {
                                        it.isSynced = true
                                        productsToBeAdded.add(it)
                                    }
                                } else {
                                    //If shopping list does not exists in database
                                    productsToBeAdded.add(it)
                                }
                            }
                            when (val savedProducts = mProductRepository.saveProducts(productsToBeAdded)) {
                                is SuccessState -> return SuccessState(Unit)
                                is ErrorState -> return ErrorState(savedProducts.error)
                                is LoadingState -> return LoadingState()
                            }

                        }
                        is ErrorState -> return ErrorState(productsRoom.error)
                        is LoadingState -> return LoadingState()
                    }
                }
            }
            is ErrorState -> return ErrorState(fetchedProducts.error)
        }
        return ErrorState(UnknownError)
    }
}