package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import com.aleksej.makaji.listopia.di.annotation.Local
import com.aleksej.makaji.listopia.di.annotation.Remote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
@Singleton
class UserRepository @Inject constructor(@Remote private val mRemoteUserDataSource: UserDataSource,
                                         @Local private val mLocalUserDataSource: UserDataSource): UserDataSource {
    override suspend fun saveUsers(users: List<UserModel>): State<List<Long>> {
        return mLocalUserDataSource.saveUsers(users)
    }

    override suspend fun getUserByIdSuspended(userId: String): State<UserModel> {
        return mLocalUserDataSource.getUserByIdSuspended(userId)
    }

    override suspend fun saveUserRemote(userModel: UserModel): State<Unit> {
        return mRemoteUserDataSource.saveUserRemote(userModel)
    }

    override suspend fun saveUser(saveUserValue: SaveUserValue): State<Long> {
        return mLocalUserDataSource.saveUser(saveUserValue)
    }

    override fun getUserById(userId: String): LiveData<StateHandler<UserModel>> {
        return mLocalUserDataSource.getUserById(userId)
    }
}