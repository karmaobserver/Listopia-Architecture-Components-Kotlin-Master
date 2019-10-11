package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
interface UserDataSource {
    suspend fun saveUser(saveUserValue: SaveUserValue): State<Long>
    suspend fun saveUserRemote(userModel: UserModel): State<Unit>
    fun getUserById(userId: String): LiveData<StateHandler<UserModel>>
    suspend fun getUserByIdSuspended(userId: String) : State<UserModel>
}
