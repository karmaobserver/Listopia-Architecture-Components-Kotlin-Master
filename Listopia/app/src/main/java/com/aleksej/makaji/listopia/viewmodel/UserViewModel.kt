package com.aleksej.makaji.listopia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.usecase.GetUserUseCase
import com.aleksej.makaji.listopia.data.usecase.SaveUserUseCase
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class UserViewModel @Inject constructor(private val mSaveUserUseCase: SaveUserUseCase,
                                        private val mGetUserUseCase: GetUserUseCase) : ViewModel() {

    private val _saveUser = MutableLiveData<SaveUserValue>()
    val saveUserLiveData = Transformations.switchMap(_saveUser) {
        mSaveUserUseCase.invoke(it)
    }

    private val _getUser = MutableLiveData<Unit>()
    val getUserLiveData = Transformations.switchMap(_getUser) {
        mGetUserUseCase.invoke(it)
    }

    fun saveUser(saveUserValue: SaveUserValue) {
        _saveUser.postValue(saveUserValue)
    }

    fun getUser() {
        _getUser.postValue(Unit)
    }
}