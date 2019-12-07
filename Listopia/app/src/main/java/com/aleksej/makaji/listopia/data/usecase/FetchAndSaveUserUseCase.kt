package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.FetchAndSaveUserValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class FetchAndSaveUserUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<FetchAndSaveUserValue, UserModel>() {

    override suspend fun invoke(value: FetchAndSaveUserValue): State<UserModel> {
        val newUserModel = UserModel(value.id, value.name, value.avatar)
        return when (val saveUserRoom = mUserRepository.saveUser(newUserModel)) {
            is SuccessState -> {
                when (val saveUserRemote =  mUserRepository.saveUserRemote(newUserModel)) {
                    is SuccessState -> SuccessState(newUserModel)
                    is LoadingState -> LoadingState()
                    is ErrorState -> ErrorState(saveUserRemote.error)
                }
            }
            is LoadingState -> LoadingState()
            is ErrorState -> ErrorState(saveUserRoom.error)
        }
    }
}