package com.aleksej.makaji.listopia.viewmodel

import androidx.lifecycle.*
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.SaveUserUseCase
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class UserViewModel @Inject constructor(private val mSaveUserUseCase: SaveUserUseCase,
                                        private val mUserRepository: UserRepository) : ViewModel() {

    private val getUserTrigger = MutableLiveData<String>()
    val getUserLiveData = Transformations.switchMap(getUserTrigger) { mUserRepository.getUserById(it) }

    private val saveUserTrigger = MutableLiveData<StateHandler<Long>>()
    val saveUserLiveData : LiveData<StateHandler<Long>> = saveUserTrigger

    private val addFriendEventTrigger = MutableLiveData<StateHandler<Unit>>()
    val addFriendEvent : LiveData<StateHandler<Unit>> = addFriendEventTrigger

    fun addFriendEvent() {
        addFriendEventTrigger.postValue(StateHandler.success(Unit))
    }

    fun getUserById(userId: String) {
        getUserTrigger.postValue(userId)
    }

    fun saveUser(saveUserValue: SaveUserValue) {
        viewModelScope.launch {
            saveUserTrigger.value = StateHandler(mSaveUserUseCase.invoke(saveUserValue))
        }
    }
}