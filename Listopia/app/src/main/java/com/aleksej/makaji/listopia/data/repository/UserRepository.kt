package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.SaveFriendValue
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
    override suspend fun deleteFriendByIdRemote(friendId: String): State<Unit> {
        return mRemoteUserDataSource.deleteFriendByIdRemote(friendId)
    }

    override suspend fun deleteFriendById(friendId: String): State<Unit> {
        return mLocalUserDataSource.deleteFriendById(friendId)
    }

    override suspend fun saveFriendByModel(userModel: UserModel): State<Long> {
        return mLocalUserDataSource.saveFriendByModel(userModel)
    }

    override suspend fun fetchUser(userId: String): State<UserModel> {
        return mRemoteUserDataSource.fetchUser(userId)
    }

    override suspend fun saveFriend(saveFriendValue: SaveFriendValue): State<Long> {
        return mLocalUserDataSource.saveFriend(saveFriendValue)
    }

    override suspend fun saveFriendRemote(saveFriendValue: SaveFriendValue): State<UserModel> {
        return mRemoteUserDataSource.saveFriendRemote(saveFriendValue)
    }

    override suspend fun saveUsers(users: List<UserModel>): State<List<Long>> {
        return mLocalUserDataSource.saveUsers(users)
    }

    override suspend fun getUserByIdSuspended(userId: String): State<UserModel> {
        return mLocalUserDataSource.getUserByIdSuspended(userId)
    }

    override suspend fun saveUserRemote(userModel: UserModel): State<Unit> {
        return mRemoteUserDataSource.saveUserRemote(userModel)
    }

    override suspend fun saveUser(userModel: UserModel): State<Long> {
        return mLocalUserDataSource.saveUser(userModel)
    }

    override fun getUserById(userId: String): LiveData<StateHandler<UserModel>> {
        return mLocalUserDataSource.getUserById(userId)
    }
}