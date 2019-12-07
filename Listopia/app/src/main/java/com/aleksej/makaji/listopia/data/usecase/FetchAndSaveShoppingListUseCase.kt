package com.aleksej.makaji.listopia.data.usecase

import android.content.Context
import androidx.room.Transaction
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.ShoppingListRepository
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.FetchAndSaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import com.aleksej.makaji.listopia.error.UnknownError
import com.aleksej.makaji.listopia.util.NotificationBarHandler
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.aleksej.makaji.listopia.util.isForeground
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-01.
 */
class FetchAndSaveShoppingListUseCase @Inject constructor(private val mShoppingListRepository: ShoppingListRepository,
                                                          private val mUserRepository: UserRepository,
                                                          private val mSharedPreferenceManager: SharedPreferenceManager,
                                                          private val mContext: Context) : UseCase<FetchAndSaveShoppingListValue, String>() {

    override suspend fun invoke(value: FetchAndSaveShoppingListValue): State<String> {
        when (val fetchedShoppingList = mShoppingListRepository.fetchShoppingListById(value.shoppingListId)) {
            is SuccessState -> {
                fetchedShoppingList.data?.let { remoteShoppingList ->
                    when (val shoppingListRoom = mShoppingListRepository.getShoppingListByIdSuspend(ShoppingListByIdValue(value.shoppingListId))) {
                        is SuccessState -> {
                            val roomShoppingList = shoppingListRoom.data
                            val editorsToBeAdded = arrayListOf<UserModel>()
                            val shoppingListEditorsRef = arrayListOf<SaveShoppingListEditorValue>()
                            val shoppingListsToBeAdded = arrayListOf<ShoppingListModel>()
                            remoteShoppingList.editors?.forEach {editor ->
                                shoppingListEditorsRef.add(SaveShoppingListEditorValue(remoteShoppingList.id, editor.id))
                                if (!editorsToBeAdded.contains(editor)) {
                                    editorsToBeAdded.add(editor)
                                }
                            }
                            if (roomShoppingList != null) {
                                //If shopping list exist in database
                                //TODO Add additional rules
                                if (remoteShoppingList.timestamp > roomShoppingList.timestamp) {
                                    remoteShoppingList.isSynced = true
                                    shoppingListsToBeAdded.add(remoteShoppingList)
                                }
                            } else {
                                //If shopping list does not exist in database
                                shoppingListsToBeAdded.add(remoteShoppingList)
                            }
                            saveShoppingListsWithEditors(shoppingListsToBeAdded, editorsToBeAdded, shoppingListEditorsRef)
                            return SuccessState(remoteShoppingList.id)

                        }
                        is ErrorState -> return ErrorState(shoppingListRoom.error)
                        is LoadingState -> return LoadingState()
                    }
                }
            }
            is ErrorState -> return ErrorState(fetchedShoppingList.error)
        }
        return ErrorState(UnknownError)
    }

    @Transaction
    private suspend fun saveShoppingListsWithEditors(shoppingListsToBeAdded: List<ShoppingListModel>, editorsToBeAdded: List<UserModel>, shoppingListEditorsRef: List<SaveShoppingListEditorValue>) {
        val editorsId = arrayListOf<String>()
        editorsToBeAdded.forEach {
            editorsId.add(it.id)
        }
        if (editorsToBeAdded.isNotEmpty() && !editorsId.contains(mSharedPreferenceManager.userId)) {
            mShoppingListRepository.deleteShoppingListById(DeleteShoppingListValue(shoppingListsToBeAdded[0].id))
            mShoppingListRepository.deleteShoppingListsWithEditorsById(DeleteShoppingListValue(shoppingListsToBeAdded[0].id))
        } else if (shoppingListsToBeAdded.isNotEmpty()) {
            mShoppingListRepository.deleteShoppingListsWithEditorsById(DeleteShoppingListValue(shoppingListsToBeAdded[0].id))
            mShoppingListRepository.saveShoppingLists(shoppingListsToBeAdded)
            if (!mContext.isForeground()) {
                NotificationBarHandler.showShoppingListMesssageNotification(mContext.getString(R.string.notification_shopping_list_updated), shoppingListsToBeAdded[0].name, mContext, shoppingListsToBeAdded[0].id)
            }
        }
        if (editorsToBeAdded.isNotEmpty()) mUserRepository.saveUsers(editorsToBeAdded)
        if (shoppingListEditorsRef.isNotEmpty()) {
            mShoppingListRepository.saveShoppingListsWithEditors(shoppingListEditorsRef)
        }
    }
}