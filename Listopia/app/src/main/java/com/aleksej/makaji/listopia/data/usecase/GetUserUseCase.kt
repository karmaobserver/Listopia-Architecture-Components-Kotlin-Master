package com.aleksej.makaji.listopia.data.usecase

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class GetUserUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<Unit, LiveData<StateHandler<UserModel>>> {

    override fun invoke(value: Unit): LiveData<StateHandler<UserModel>> {
        return mUserRepository.getUser()
    }
}