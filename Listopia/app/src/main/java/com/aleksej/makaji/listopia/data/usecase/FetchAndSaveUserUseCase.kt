package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.FetchAndSaveUserValue
import com.aleksej.makaji.listopia.error.BackendError
import com.aleksej.makaji.listopia.error.ERROR_NOT_FOUND
import com.aleksej.makaji.listopia.error.UnknownError
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class FetchAndSaveUserUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<FetchAndSaveUserValue, Long>() {

    override suspend fun invoke(value: FetchAndSaveUserValue): State<Long> {
        val returnValue : State<Long> = ErrorState(UnknownError)
        when (val fetchedUser = mUserRepository.fetchUser(value.id)) {
            is SuccessState -> {
                fetchedUser.data?.let {
                    return mUserRepository.saveUser(it)
                }
            }
            is LoadingState -> {}
            is ErrorState -> {
                val error = fetchedUser.error as BackendError
                if (error.response.code == ERROR_NOT_FOUND) {
                    //User has never been logged to system yet
                    val newUserModel = UserModel(value.id, value.name, value.avatar)
                    when (val saveUserRoom = mUserRepository.saveUser(newUserModel)) {
                        is SuccessState -> {
                            when (val saveUserRemote =  mUserRepository.saveUserRemote(newUserModel)) {
                                is SuccessState -> {}
                                is LoadingState -> {}
                                is ErrorState -> return ErrorState(saveUserRemote.error)
                            }
                        }
                        else -> return saveUserRoom
                    }
                } else {
                    return ErrorState(fetchedUser.error)
                }
            }
        }
        return returnValue
    }
}