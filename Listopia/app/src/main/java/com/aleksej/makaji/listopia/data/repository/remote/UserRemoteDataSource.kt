package com.aleksej.makaji.listopia.data.repository.remote

import androidx.lifecycle.LiveData
import com.aleksej.makaji.listopia.data.api.ListopiaApi
import com.aleksej.makaji.listopia.data.api.callback.CoroutineAdapter
import com.aleksej.makaji.listopia.data.api.dto.request.FetchFriendsRequest
import com.aleksej.makaji.listopia.data.api.dto.request.UpdateFirebaseTokenRequest
import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.mapper.mapToDeleteEditorRequest
import com.aleksej.makaji.listopia.data.mapper.mapToSaveEditorRequest
import com.aleksej.makaji.listopia.data.mapper.mapToSaveFriendRequest
import com.aleksej.makaji.listopia.data.mapper.mapToSaveUserRequest
import com.aleksej.makaji.listopia.data.repository.UserDataSource
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveFriendValue
import com.aleksej.makaji.listopia.error.ExceptionError
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
@Singleton
class UserRemoteDataSource @Inject constructor(private val mListopiaApi: ListopiaApi, private val mRetrofit: Retrofit, private val mSharedPreferenceManager: SharedPreferenceManager) : UserDataSource {
    override suspend fun clearDatabase(): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteFriendById(friendId: String): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteFriendByIdRemote(friendId: String): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.deleteFriendById(mSharedPreferenceManager.userId, friendId), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun fetchUser(userId: String): State<UserModel> {
        return try {
            CoroutineAdapter(mListopiaApi.fetchUserById(userId), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun saveFriend(saveFriendValue: SaveFriendValue): State<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveFriendRemote(saveFriendValue: SaveFriendValue): State<UserModel> {
        return try {
            CoroutineAdapter(mListopiaApi.saveFriend(mSharedPreferenceManager.userId, saveFriendValue.mapToSaveFriendRequest()), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun saveUserRemote(userModel: UserModel): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.saveUser(userModel.mapToSaveUserRequest()), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun saveEditor(saveEditorValue: SaveEditorValue): State<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveEditorRemote(saveEditorValue: SaveEditorValue): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.saveEditor(saveEditorValue.mapToSaveEditorRequest()), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun deleteEditor(deleteEditorValue: DeleteEditorValue): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteEditorRemote(deleteEditorValue: DeleteEditorValue): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.deleteEditor(deleteEditorValue.mapToDeleteEditorRequest()), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun fetchFriends(friendsId: List<String>): State<List<UserModel>> {
        return try {
            CoroutineAdapter(mListopiaApi.fetchFriends(FetchFriendsRequest(friendsId)), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun updateFirebaseToken(token: String): State<Unit> {
        return try {
            CoroutineAdapter(mListopiaApi.updateFirebaseToken(UpdateFirebaseTokenRequest(token, mSharedPreferenceManager.userId)), mRetrofit)()
        } catch (e: Exception) {
            ErrorState(ExceptionError(e))
        }
    }

    override suspend fun saveUsers(users: List<UserModel>): State<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserByIdSuspended(userId: String): State<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveUser(userModel: UserModel): State<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserById(userId: String): LiveData<StateHandler<UserModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveFriendByModel(userModel: UserModel): State<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}