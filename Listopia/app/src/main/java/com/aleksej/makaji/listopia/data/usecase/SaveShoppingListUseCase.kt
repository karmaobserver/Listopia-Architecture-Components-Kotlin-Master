package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.util.Validator
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/19/19.
 */
class SaveShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<SaveShoppingListValue, Long>() {

    override suspend fun invoke(value: SaveShoppingListValue): State<Long> {
        Validator.validateListName(value.name)?.run {
            return ErrorState(this)
        }
        return mShoppingListRepository.saveShoppingList(value)
    }
}