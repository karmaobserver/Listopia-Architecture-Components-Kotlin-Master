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
class FetchAndSaveUserUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<FetchAndSaveUserValue, UserModel>() {

    override suspend fun invoke(value: FetchAndSaveUserValue): State<UserModel> {
        val returnValue : State<UserModel> = ErrorState(UnknownError)
        when (val fetchedUser = mUserRepository.fetchUser(value.id)) {
            is SuccessState -> {
                fetchedUser.data?.let {
                    return when (val userRoom = mUserRepository.saveUser(it)) {
                        is SuccessState -> SuccessState(it)
                        is LoadingState -> LoadingState()
                        is ErrorState -> ErrorState(userRoom.error)
                    }
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
                            return when (val saveUserRemote =  mUserRepository.saveUserRemote(newUserModel)) {
                                is SuccessState -> SuccessState(newUserModel)
                                is LoadingState -> LoadingState()
                                is ErrorState -> ErrorState(saveUserRemote.error)
                            }
                        }
                        is LoadingState -> {}
                        is ErrorState -> return ErrorState(saveUserRoom.error)
                    }
                } else {
                    return ErrorState(fetchedUser.error)
                }
            }
        }
        return returnValue
    }
}