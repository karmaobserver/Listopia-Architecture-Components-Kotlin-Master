package com.aleksej.makaji.listopia.viewmodel

import androidx.lifecycle.*
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.DeleteFriendByIdUseCase
import com.aleksej.makaji.listopia.data.usecase.SaveFriendUseCase
import com.aleksej.makaji.listopia.data.usecase.FetchAndSaveUserUseCase
import com.aleksej.makaji.listopia.data.usecase.value.DeleteFriendValue
import com.aleksej.makaji.listopia.data.usecase.value.FetchAndSaveUserValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveFriendValue
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class UserViewModel @Inject constructor(private val mFetchAndSaveUserUseCase: FetchAndSaveUserUseCase,
                                        private val mUserRepository: UserRepository,
                                        private val mSaveFriendUseCase: SaveFriendUseCase,
                                        private val mDeleteFriendByIdUseCase: DeleteFriendByIdUseCase) : ViewModel() {

    private val getUserTrigger = MutableLiveData<String>()
    val getUserLiveData = Transformations.switchMap(getUserTrigger) { mUserRepository.getUserById(it) }

    private val fetchAndSaveUserTrigger = MutableLiveData<StateHandler<Long>>()
    val fetchAndSaveUserLiveData : LiveData<StateHandler<Long>> = fetchAndSaveUserTrigger


    private val deleteFriendTrigger = MutableLiveData<StateHandler<Unit>>()
    val deleteFriendEventLiveData : LiveData<StateHandler<Unit>> = deleteFriendTrigger

    private val saveFriendTrigger = MutableLiveData<StateHandler<Long>>()
    val saveFriendLiveData : LiveData<StateHandler<Long>> = saveFriendTrigger

    private val addFriendEventTrigger = MutableLiveData<StateHandler<Unit>>()
    val addFriendEvent : LiveData<StateHandler<Unit>> = addFriendEventTrigger


    fun addFriendEvent() {
        addFriendEventTrigger.postValue(StateHandler.success(Unit))
    }

    fun getUserById(userId: String) {
        getUserTrigger.postValue(userId)
    }

    fun fetchAndSaveUser(fetchAndSaveUserValue: FetchAndSaveUserValue) {
        viewModelScope.launch {
            fetchAndSaveUserTrigger.value = StateHandler(mFetchAndSaveUserUseCase.invoke(fetchAndSaveUserValue))
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
}