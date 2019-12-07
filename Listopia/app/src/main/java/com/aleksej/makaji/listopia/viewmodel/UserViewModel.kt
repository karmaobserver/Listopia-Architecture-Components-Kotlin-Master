package com.aleksej.makaji.listopia.viewmodel

import androidx.lifecycle.*
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.*
import com.aleksej.makaji.listopia.data.usecase.value.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class UserViewModel @Inject constructor(private val mFetchAndSaveUserUseCase: FetchAndSaveUserUseCase,
                                        private val mUserRepository: UserRepository,
                                        private val mSaveFriendUseCase: SaveFriendUseCase,
                                        private val mDeleteFriendByIdUseCase: DeleteFriendByIdUseCase,
                                        private val mSaveEditorUseCase: SaveEditorUseCase,
                                        private val mDeleteEditorUseCase: DeleteEditorUseCase,
                                        private val mUpdateFirebaseTokenUseCase: UpdateFirebaseTokenUseCase,
                                        private val mFetchAndSaveFriendsUseCase: FetchAndSaveFriendsUseCase,
                                        private val mRemoveSessionUseCase: RemoveSessionUseCase) : ViewModel() {

    private val getUserTrigger = MutableLiveData<String>()
    val getUserLiveData = Transformations.switchMap(getUserTrigger) { mUserRepository.getUserById(it) }

    private val fetchAndSaveUserTrigger = MutableLiveData<StateHandler<UserModel>>()
    val fetchAndSaveUserLiveData : LiveData<StateHandler<UserModel>> = fetchAndSaveUserTrigger

    private val fetchAndSaveFriendsTrigger = MutableLiveData<StateHandler<Unit>>()
    val fetchAndSaveFriendsLiveData : LiveData<StateHandler<Unit>> = fetchAndSaveFriendsTrigger

    private val deleteFriendTrigger = MutableLiveData<StateHandler<Unit>>()
    val deleteFriendEventLiveData : LiveData<StateHandler<Unit>> = deleteFriendTrigger

    private val removeSessionTrigger = MutableLiveData<StateHandler<Unit>>()
    val removeSessioEventLiveData : LiveData<StateHandler<Unit>> = removeSessionTrigger


    private val updateTokenTrigger = MutableLiveData<StateHandler<Unit>>()
    val updateTokenEventLiveData : LiveData<StateHandler<Unit>> = updateTokenTrigger

    private val saveFriendTrigger = MutableLiveData<StateHandler<Long>>()
    val saveFriendLiveData : LiveData<StateHandler<Long>> = saveFriendTrigger

    private val saveEditorTrigger = MutableLiveData<StateHandler<Long>>()
    val saveEditorLiveData : LiveData<StateHandler<Long>> = saveEditorTrigger

    private val deleteEditorTrigger = MutableLiveData<StateHandler<Unit>>()
    val deleteEditorLiveData : LiveData<StateHandler<Unit>> = deleteEditorTrigger

    private val addFriendEventTrigger = MutableLiveData<StateHandler<Unit>>()
    val addFriendEvent : LiveData<StateHandler<Unit>> = addFriendEventTrigger


    fun addFriendEvent() {
        addFriendEventTrigger.postValue(StateHandler.success(Unit))
    }

    fun getUserById(userId: String) {
        getUserTrigger.postValue(userId)
    }

    fun fetchAndSaveUser(fetchAndSaveUserValue: FetchAndSaveUserValue) {
        fetchAndSaveUserTrigger.postValue(StateHandler.loading())
        GlobalScope.launch {
            fetchAndSaveUserTrigger.postValue(StateHandler(mFetchAndSaveUserUseCase.invoke(fetchAndSaveUserValue)))
        }
    }

    fun fetchAndSaveFriends(fetchAndSaveFriendsValue: FetchAndSaveFriendsValue) {
        fetchAndSaveFriendsTrigger.value = StateHandler.loading()
        viewModelScope.launch {
            fetchAndSaveFriendsTrigger.value = StateHandler(mFetchAndSaveFriendsUseCase.invoke(fetchAndSaveFriendsValue))
        }
    }

    fun saveFriend(saveFriendValue: SaveFriendValue) {
        saveFriendTrigger.value = StateHandler.loading()
        viewModelScope.launch {
            saveFriendTrigger.value = StateHandler(mSaveFriendUseCase.invoke(saveFriendValue))
        }
    }

    fun deleteFriendById(deleteFriendValue: DeleteFriendValue) {
        deleteFriendTrigger.value = StateHandler.loading()
        viewModelScope.launch {
            deleteFriendTrigger.value = StateHandler(mDeleteFriendByIdUseCase.invoke(deleteFriendValue))
        }
    }

    fun saveEditor(saveEditorValue: SaveEditorValue) {
        saveEditorTrigger.value = StateHandler.loading()
        viewModelScope.launch {
            saveEditorTrigger.value = StateHandler(mSaveEditorUseCase.invoke(saveEditorValue))
        }
    }

    fun deleteEditor(deleteEditorValue: DeleteEditorValue) {
        deleteEditorTrigger.value = StateHandler.loading()
        viewModelScope.launch {
            deleteEditorTrigger.value = StateHandler(mDeleteEditorUseCase.invoke(deleteEditorValue))
        }
    }

    fun updateFirebaseToken() {
        GlobalScope.launch {
            updateTokenTrigger.postValue(StateHandler(mUpdateFirebaseTokenUseCase.invoke((Unit))))
        }
    }

    fun removeSession() {
        removeSessionTrigger.value = StateHandler.loading()
        GlobalScope.launch {
            removeSessionTrigger.postValue(StateHandler(mRemoveSessionUseCase.invoke(Unit)))
        }
    }
}