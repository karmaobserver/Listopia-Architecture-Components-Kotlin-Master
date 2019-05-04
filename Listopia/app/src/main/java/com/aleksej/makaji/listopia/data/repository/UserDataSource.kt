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
    suspend fun saveUser(saveUserValue: SaveUserValue): State<Unit>
    fun getUser(): LiveData<StateHandler<UserModel>>
}