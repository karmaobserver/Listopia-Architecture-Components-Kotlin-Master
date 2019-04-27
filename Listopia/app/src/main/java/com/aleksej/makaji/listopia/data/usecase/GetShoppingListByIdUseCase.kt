package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/3/19.
 */
class GetShoppingListByIdUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<ShoppingListByIdValue, LiveData<StateHandler<ShoppingListModel>>> {

    override fun invoke(value: ShoppingListByIdValue): LiveData<StateHandler<ShoppingListModel>> {
        return mShoppingListRepository.getShoppingListById(value)
    }
}