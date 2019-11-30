package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.DeleteProductsValue
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
class DeleteShoppingListByIdUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository,
                                                        private val mProductRepository: ProductRepository) : UseCase<DeleteShoppingListValue, Int>() {

    override suspend fun invoke(value: DeleteShoppingListValue): State<Int> {
        return when (val deletedProductsForShoppingList = mProductRepository.deleteProductsByShoppingList(DeleteProductsValue(value.id))) {
            is SuccessState -> {
                when (val deletedShoppingListRoom = mShoppingListRepository.deleteShoppingListById(value)) {
                    is SuccessState -> {
                        when (val deleteShoppingListRemote = mShoppingListRepository.deleteShoppingListByIdRemote(value.id)) {
                            is SuccessState -> deletedShoppingListRoom
                            is LoadingState -> LoadingState()
                            is ErrorState -> ErrorState(deleteShoppingListRemote.error)
                        }
                    }
                    else -> deletedShoppingListRoom
                }
            }
            is LoadingState -> LoadingState()
            is ErrorState -> ErrorState(deletedProductsForShoppingList.error)
        }
    }
}