package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class SaveUserUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<SaveUserValue, LiveData<StateHandler<Unit>>> {

    private val saveUserLiveData = MutableLiveData<StateHandler<Unit>>()

    override fun invoke(value: SaveUserValue): LiveData<StateHandler<Unit>> {
        GlobalScope.launch {
            saveUserLiveData.postValue(StateHandler.loading())
            val saveProductResponse = mUserRepository.saveUser(value)
            saveUserLiveData.postValue(StateHandler(saveProductResponse))
        }
        return saveUserLiveData
    }
}