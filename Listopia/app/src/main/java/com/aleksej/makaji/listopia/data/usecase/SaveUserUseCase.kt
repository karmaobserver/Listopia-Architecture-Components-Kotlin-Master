package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class SaveUserUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<SaveUserValue, UserModel>() {

    override suspend fun invoke(value: SaveUserValue): State<UserModel> {
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