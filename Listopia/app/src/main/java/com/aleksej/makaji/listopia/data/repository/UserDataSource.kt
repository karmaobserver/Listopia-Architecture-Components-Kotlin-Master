package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.SaveFriendValue

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
interface UserDataSource {
    suspend fun fetchUser(userId: String): State<UserModel>
    suspend fun saveUser(userModel: UserModel): State<Long>
    suspend fun saveUsers(users: List<UserModel>): State<List<Long>>
    suspend fun saveUserRemote(userModel: UserModel): State<Unit>
    fun getUserById(userId: String): LiveData<StateHandler<UserModel>>
    suspend fun getUserByIdSuspended(userId: String) : State<UserModel>
    suspend fun saveFriend(saveFriendValue: SaveFriendValue): State<Long>
    suspend fun saveFriendRemote(saveFriendValue: SaveFriendValue): State<Unit>
}
