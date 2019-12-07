package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-05.
 */
class RemoveSessionUseCase @Inject constructor(private val mUserRepository: UserRepository,
                                               private val mSharedPreferenceManager: SharedPreferenceManager): UseCase<Unit, Unit>() {
    override suspend fun invoke(value: Unit): State<Unit> {
        mUserRepository.clearDatabase()
        FirebaseInstanceId.getInstance().deleteInstanceId()
        mSharedPreferenceManager.clearAll()
        return SuccessState(Unit)
    }
}