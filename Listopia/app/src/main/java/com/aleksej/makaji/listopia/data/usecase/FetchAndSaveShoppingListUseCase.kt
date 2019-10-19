package com.aleksej.makaji.listopia.data.usecase

import androidx.room.Transaction
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.FetchShoppingListsValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.error.UnknownError
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-10-12.
 */
class FetchAndSaveShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository,
                                                          private val mUserRepository: UserRepository) : UseCase<FetchShoppingListsValue, Unit>() {

    override suspend fun invoke(value: FetchShoppingListsValue): State<Unit> {
        when (val fetchedShoppingLists = mShoppingListRepository.fetchShoppingListsByUserId(value)) {
            is SuccessState -> {
                fetchedShoppingLists.data?.let { remoteShoppingLists ->
                    when (val shoppingListsRoom = mShoppingListRepository.getShoppingListsSuspend()) {
                        is SuccessState -> {
                            val roomShoppingLists = shoppingListsRoom.data
                            val editorsToBeAdded = arrayListOf<UserModel>()
                            val shoppingListEditorsRef = arrayListOf<SaveShoppingListEditorValue>()
                            val shoppingListsToBeAdded = arrayListOf<ShoppingListModel>()
                            remoteShoppingLists.forEach {
                                it.editors?.forEach {editor ->
                                    shoppingListEditorsRef.add(SaveShoppingListEditorValue(it.id, editor.id))
                                    if (!editorsToBeAdded.contains(editor)) {
                                        editorsToBeAdded.add(editor)
                                    }
                                }
                                val index = roomShoppingLists?.indexOfFirst { roomMessageModel ->
                                    roomMessageModel.id == it.id
                                } // -1 if not found
                                if (index != null && index >= 0) {
                                    //If shopping list exists in database
                                    //TODO Add additional rules
                                    val roomShoppingListModel = roomShoppingLists[index]
                                    if (it.timestamp > roomShoppingListModel.timestamp) {
                                        it.isSynced = true
                                        shoppingListsToBeAdded.add(it)
                                    }
                                } else {
                                    //If shopping list does not exists in database
                                    shoppingListsToBeAdded.add(it)
                                }
                            }
                            saveShoppingListsWithEditors(shoppingListsToBeAdded, editorsToBeAdded, shoppingListEditorsRef)
                            return SuccessState(Unit)

                        }
                        is ErrorState -> return ErrorState(shoppingListsRoom.error)
                        is LoadingState -> {}
                    }
                }
            }
            is ErrorState -> return ErrorState(fetchedShoppingLists.error)
        }
        return ErrorState(UnknownError)
    }

    @Transaction
    private suspend fun saveShoppingListsWithEditors(shoppingListsToBeAdded: List<ShoppingListModel>, editorsToBeAdded: List<UserModel>, shoppingListEditorsRef: List<SaveShoppingListEditorValue>) {
        if (shoppingListsToBeAdded.isNotEmpty()) mShoppingListRepository.saveShoppingLists(shoppingListsToBeAdded)
        if (editorsToBeAdded.isNotEmpty()) mUserRepository.saveUsers(editorsToBeAdded)
        if (shoppingListEditorsRef.isNotEmpty()) mShoppingListRepository.saveShoppingListsWithEditors(shoppingListEditorsRef)
    }
}