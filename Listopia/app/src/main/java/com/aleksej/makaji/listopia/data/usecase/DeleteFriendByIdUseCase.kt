package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.value.DeleteFriendValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-11-24.
 */
class DeleteFriendByIdUseCase @Inject constructor(private val mUserRepository: UserRepository) : UseCase<DeleteFriendValue, Unit>() {

    override suspend fun invoke(value: DeleteFriendValue): State<Unit> {
        return when (val deleteFriendRemote = mUserRepository.deleteFriendByIdRemote(value.friendId)) {
            is SuccessState -> {
                mUserRepository.deleteFriendById(value.friendId)
            }
            else -> deleteFriendRemote
        }
    }
}