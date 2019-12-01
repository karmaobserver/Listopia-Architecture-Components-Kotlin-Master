package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.util.asDeferred
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-01.
 */
class UpdateFirebaseTokenUseCase @Inject constructor(private val mUserRepository: UserRepository) : UseCase<Unit, Unit>(){
    override suspend fun invoke(value: Unit): State<Unit> {
        val firebaseResult = FirebaseInstanceId.getInstance().instanceId.asDeferred().await()
        return mUserRepository.updateFirebaseToken(firebaseResult.token)
    }
}