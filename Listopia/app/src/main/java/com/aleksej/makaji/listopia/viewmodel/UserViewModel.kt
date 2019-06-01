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

    private val getUserTrigger = MutableLiveData<Unit>()
    val getUserLiveData = Transformations.switchMap(getUserTrigger) { mUserRepository.getUser() }

    private val saveUserTrigger = MutableLiveData<StateHandler<Unit>>()
    val saveUserLiveData : LiveData<StateHandler<Unit>> = saveUserTrigger

    fun getUser() {
        getUserTrigger.postValue(Unit)
    }

    fun saveUser(saveUserValue: SaveUserValue) {
        viewModelScope.launch {
            saveUserTrigger.value = StateHandler(mSaveUserUseCase.invoke(saveUserValue))
        }
    }
}