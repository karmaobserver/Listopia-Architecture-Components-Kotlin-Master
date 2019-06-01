package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import com.aleksej.makaji.listopia.util.Validator
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
class UpdateShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<ShoppingListValue, Int>() {

    override suspend fun invoke(value: ShoppingListValue): State<Int> {
        Validator.validateListName(value.name)?.run {
            return ErrorState(this)
        }
        return  mShoppingListRepository.updateShoppingList(value)
    }
}