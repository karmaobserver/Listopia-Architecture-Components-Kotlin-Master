package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
class SaveUserUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<SaveUserValue, Unit>() {

    override suspend fun invoke(value: SaveUserValue): State<Unit> {
        return mUserRepository.saveUser(value)
    }
}