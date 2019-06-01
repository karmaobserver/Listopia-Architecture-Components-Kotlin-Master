package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
class DeleteShoppingListByIdUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<DeleteShoppingListValue, Int>() {

    override suspend fun invoke(value: DeleteShoppingListValue): State<Int> {
        return mShoppingListRepository.deleteShoppingListById(value)
    }
}