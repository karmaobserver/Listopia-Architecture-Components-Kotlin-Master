package com.aleksej.makaji.listopia.data.repository

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.enums.SourceType
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
    override suspend fun saveUser(saveUserValue: SaveUserValue): State<Unit> {
        return if (saveUserValue.sourceType == SourceType.LOCAL_ONLY) {
            mLocalUserDataSource.saveUser(saveUserValue)
        } else {
            mRemoteUserDataSource.saveUser(saveUserValue)
        }
    }

    override fun getUser(): LiveData<StateHandler<UserModel>> {
        return mLocalUserDataSource.getUser()
    }
}