package com.aleksej.makaji.listopia.data.repository.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.mapper.mapToUser
import com.aleksej.makaji.listopia.data.mapper.mapToUserModel
import com.aleksej.makaji.listopia.data.repository.UserDataSource
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.room.dao.UserDao
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import com.aleksej.makaji.listopia.error.RoomError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
@Singleton
class UserLocalDataSource @Inject constructor(private val mUserDao: UserDao) : UserDataSource, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job()

    private val userLiveData = MutableLiveData<StateHandler<UserModel>>()

    override suspend fun saveUser(saveUserValue: SaveUserValue): State<Unit> {
        return try {
            SuccessState(mUserDao.saveUser(saveUserValue.mapToUser()))
        }catch (e: Exception){
            ErrorState(RoomError)
        }
    }

    override fun getUser(): LiveData<StateHandler<UserModel>> {
        userLiveData.postValue(StateHandler.loading())
        try {
            return Transformations.switchMap(mUserDao.getUser()) {
                it?.run {
                    userLiveData.postValue(StateHandler.success(it.mapToUserModel()))
                }
                return@switchMap userLiveData
            }
        } catch (e: Exception) {
            userLiveData.postValue(StateHandler.error(RoomError))
        }
        return userLiveData
    }
}