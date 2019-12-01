package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveFriendValue

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
interface UserDataSource {
    suspend fun fetchUser(userId: String): State<UserModel>
    suspend fun fetchFriends(friendsId: List<String>): State<List<UserModel>>
    suspend fun saveUser(userModel: UserModel): State<Long>
    suspend fun saveUsers(users: List<UserModel>): State<List<Long>>
    suspend fun saveUserRemote(userModel: UserModel): State<Unit>
    fun getUserById(userId: String): LiveData<StateHandler<UserModel>>
    suspend fun getUserByIdSuspended(userId: String) : State<UserModel>
    suspend fun saveFriend(saveFriendValue: SaveFriendValue): State<Long>
    suspend fun saveFriendByModel(userModel: UserModel): State<Long>
    suspend fun saveFriendRemote(saveFriendValue: SaveFriendValue): State<UserModel>
    suspend fun deleteFriendByIdRemote(friendId: String): State<Unit>
    suspend fun deleteFriendById(friendId: String): State<Unit>
    suspend fun saveEditor(saveEditorValue: SaveEditorValue): State<Long>
    suspend fun saveEditorRemote(saveEditorValue: SaveEditorValue): State<Unit>
    suspend fun deleteEditor(deleteEditorValue: DeleteEditorValue): State<Unit>
    suspend fun deleteEditorRemote(deleteEditorValue: DeleteEditorValue): State<Unit>
}

