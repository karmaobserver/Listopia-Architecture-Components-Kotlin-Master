package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class SaveUserUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<SaveUserValue, Long>() {

    override suspend fun invoke(value: SaveUserValue): State<Long> {
        when (val saveUserRoom = mUserRepository.saveUser(value)) {
            is SuccessState -> {
                when (val getUserRoom = mUserRepository.getUserByIdSuspended(value.id)) {
                    is SuccessState -> {
                        getUserRoom.data?.let {
                            mUserRepository.saveUserRemote(it)
                        }
                    }
                    else -> Timber.d("Failed to getUserRoom")
                }
                return saveUserRoom
            }
            else -> return saveUserRoom
        }
    }
}