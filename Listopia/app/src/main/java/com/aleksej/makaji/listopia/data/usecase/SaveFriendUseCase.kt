package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveFriendValue
import com.aleksej.makaji.listopia.error.NoRemoteData
import com.aleksej.makaji.listopia.util.Validator
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-10-19.
 */
class SaveFriendUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<SaveFriendValue, Long>() {

    override suspend fun invoke(value: SaveFriendValue): State<Long> {
        Validator.validateEmail(value.friendId)?.run {
            return ErrorState(this)
        }

        when (val saveFriendRemote =  mUserRepository.saveFriendRemote(value)) {
            is SuccessState -> {
                saveFriendRemote.data?.let {
                    return mUserRepository.saveFriendByModel(it)
                }
                return ErrorState(NoRemoteData)
            }
            is ErrorState -> {
                Timber.d("Failed to saveFriendRemote")
                return ErrorState(saveFriendRemote.error)
            }
            is LoadingState -> return LoadingState()
        }
    }
}
