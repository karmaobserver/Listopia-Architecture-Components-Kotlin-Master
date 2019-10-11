package com.aleksej.makaji.listopia.data.repository.remote

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.api.ListopiaApi
import com.aleksej.makaji.listopia.data.api.callback.CoroutineAdapter
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.mapper.mapToSaveUserRequest
import com.aleksej.makaji.listopia.data.repository.UserDataSource
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import com.aleksej.makaji.listopia.error.ExceptionError
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
@Singleton
class UserRemoteDataSource @Inject constructor(private val mListopiaApi: ListopiaApi, private val mRetrofit: Retrofit) : UserDataSource {
    override suspend fun saveUserRemote(userModel: UserModel): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.saveUser(userModel.mapToSaveUserRequest()), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun getUserByIdSuspended(userId: String): State<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveUser(saveUserValue: SaveUserValue): State<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserById(userId: String): LiveData<StateHandler<UserModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}