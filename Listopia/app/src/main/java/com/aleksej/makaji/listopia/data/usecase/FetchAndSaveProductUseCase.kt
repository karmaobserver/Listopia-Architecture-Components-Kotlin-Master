package com.aleksej.makaji.listopia.data.usecase

import android.content.Context
import androidx.room.Transaction
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.FetchAndSaveProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import com.aleksej.makaji.listopia.error.UnknownError
import com.aleksej.makaji.listopia.util.NotificationBarHandler
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.aleksej.makaji.listopia.util.isForeground
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-05.
 */
class FetchAndSaveProductUseCase @Inject constructor(private val mProductRepository: ProductRepository,
                                                     private val mShoppingListRepository: ShoppingListRepository,
                                                     private val mSharedPreferenceManager: SharedPreferenceManager,
                                                     private val mContext: Context) : UseCase<FetchAndSaveProductValue, String>() {

    override suspend fun invoke(value: FetchAndSaveProductValue): State<String> {
        when (val fetchedProduct = mProductRepository.fetchProductById(value)) {
            is SuccessState -> {
                fetchedProduct.data?.let { remoteProduct ->
                    when (val productRoom = mProductRepository.getProductByIdSuspend(value.productId)) {
                        is SuccessState -> {
                            val productRoom = productRoom.data
                            val productsToBeAdded = arrayListOf<ProductModel>()

                            if (productRoom != null) {
                                //If shopping list exist in database
                                //TODO Add additional rules
                                if (remoteProduct.timestamp > productRoom.timestamp) {
                                    remoteProduct.isSynced = true
                                    productsToBeAdded.add(remoteProduct)
                                }
                            } else {
                                //If shopping list does not exist in database
                                productsToBeAdded.add(remoteProduct)
                            }
                            saveProduct(productsToBeAdded)
                            return SuccessState(remoteProduct.id)

                        }
                        is ErrorState -> return ErrorState(productRoom.error)
                        is LoadingState -> return LoadingState()
                    }
                }
            }
            is ErrorState -> return ErrorState(fetchedProduct.error)
        }
        return ErrorState(UnknownError)
    }

    @Transaction
    private suspend fun saveProduct(productsToBeAdded: List<ProductModel>) {
        if (productsToBeAdded.isNullOrEmpty()) return
        when (val shoppingList = mShoppingListRepository.getShoppingListByIdSuspend(ShoppingListByIdValue(productsToBeAdded[0].shoppingListId))) {
            is SuccessState -> {
                val editorsId = arrayListOf<String>()
                shoppingList.data?.editors?.forEach {
                    editorsId.add(it.id)
                }
                if (!editorsId.contains(mSharedPreferenceManager.userId)) {
                    mProductRepository.deleteProductById(ProductValue(productsToBeAdded[0].id))
                } else if (productsToBeAdded.isNotEmpty()) {
                    mProductRepository.saveProducts(productsToBeAdded)
                    if (!mContext.isForeground()) {
                        NotificationBarHandler.showShoppingListMesssageNotification(mContext.getString(R.string.notification_shopping_list_updated), shoppingList.data?.name ?: mContext.getString(R.string.notification_shopping_list_updated_name), mContext, productsToBeAdded[0].shoppingListId)
                    }
                }
            }
        }
    }
}