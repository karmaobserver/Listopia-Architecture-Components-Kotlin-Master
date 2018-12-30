package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.aleksej.makaji.listopia.data.event.EventHandler
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class GetShoppingListsUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository) : UseCase<Unit, LiveData<EventHandler<PagedList<ShoppingListModel>>>> {

    override fun invoke(value: Unit): LiveData<EventHandler<PagedList<ShoppingListModel>>> {
        return mShoppingListRepository.getShoppingLists()
    }
}