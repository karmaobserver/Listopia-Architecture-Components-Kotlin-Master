package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.error.UnknownError
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-09.
 */
class FetchAndSaveUserUseCase @Inject constructor(private val mUserRepository: UserRepository,
                                                  private val mSharedPreferenceManager: SharedPreferenceManager): UseCase<Unit, UserModel>() {

    override suspend fun invoke(value: Unit): State<UserModel> {

        return when (val fetchUser = mUserRepository.fetchUser(mSharedPreferenceManager.userId)) {
            is SuccessState -> {
                fetchUser.data?.let {
                    return when (val saveUserRoom = mUserRepository.saveUser(it)) {
                        is SuccessState -> {
                            SuccessState(it)
                        }
                        is LoadingState -> LoadingState()
                        is ErrorState -> ErrorState(saveUserRoom.error)
                    }
                }
                return ErrorState(UnknownError)
            }
            is LoadingState -> LoadingState()
            is ErrorState -> ErrorState(fetchUser.error)
        }
    }
}