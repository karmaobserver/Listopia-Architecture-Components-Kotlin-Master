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
import com.aleksej.makaji.listopia.data.room.model.ShoppingListUserXRef
import com.aleksej.makaji.listopia.data.room.model.UserUserXRef
import com.aleksej.makaji.listopia.data.usecase.value.DeleteEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveFriendValue
import com.aleksej.makaji.listopia.error.RoomError
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
@Singleton
class UserLocalDataSource @Inject constructor(private val mUserDao: UserDao, private val mSharedPreferenceManager: SharedPreferenceManager) : UserDataSource, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job()

    private val userLiveData = MutableLiveData<StateHandler<UserModel>>()

    override suspend fun saveUser(userModel: UserModel): State<Long> {
        return try {
            saveFriends(userModel)
            SuccessState(mUserDao.saveUser(userModel.mapToUser()))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveUsers(users: List<UserModel>): State<List<Long>> {
        return try {
            SuccessState(mUserDao.saveUsers(users.map {
                it.mapToUser()
            }))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override fun getUserById(userId: String): LiveData<StateHandler<UserModel>> {
        userLiveData.postValue(StateHandler.loading())
        try {
            return Transformations.switchMap(mUserDao.getUserWithFriends(userId)) {
                it?.run {
                    userLiveData.postValue(StateHandler.success(it.mapToUserModel()))
                }
                return@switchMap userLiveData
            }
        } catch (e: Exception) {
            userLiveData.postValue(StateHandler.error(RoomError(e)))
        }
        return userLiveData
    }

    override suspend fun saveUserRemote(userModel: UserModel): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserByIdSuspended(userId: String): State<UserModel> {
        return try {
            SuccessState(mUserDao.getUserWithFriendsSuspended(userId).mapToUserModel())
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveFriendRemote(saveFriendValue: SaveFriendValue): State<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveFriend(saveFriendValue: SaveFriendValue): State<Long> {
        return try {
            SuccessState(mUserDao.saveUser(saveFriendValue.mapToUser()))
            SuccessState(mUserDao.saveFriend(UserUserXRef(mSharedPreferenceManager.userId, saveFriendValue.friendId)))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveFriendByModel(userModel: UserModel): State<Long> {
        return try {
            SuccessState(mUserDao.saveUser(userModel.mapToUser()))
            SuccessState(mUserDao.saveFriend(UserUserXRef(mSharedPreferenceManager.userId, userModel.id)))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun fetchUser(userId: String): State<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private suspend fun saveFriends(userModel: UserModel) {
        val friends = arrayListOf<UserUserXRef>()
        userModel.friends?.let {
            it.forEach {
                friends.add(UserUserXRef(mSharedPreferenceManager.userId, it.id))
            }
            mUserDao.saveFriends(friends)
        }
    }

    override suspend fun deleteFriendByIdRemote(friendId: String): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteFriendById(friendId: String): State<Unit> {
        return try {
            SuccessState(mUserDao.deleteFriend(mSharedPreferenceManager.userId, friendId))
            SuccessState(mUserDao.deleteUserById(friendId))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveEditor(saveEditorValue: SaveEditorValue): State<Long> {
        return try {
            SuccessState(mUserDao.saveEditor(ShoppingListUserXRef(saveEditorValue.shoppingListId, saveEditorValue.editorId)))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun saveEditorRemote(saveEditorValue: SaveEditorValue): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteEditor(deleteEditorValue: DeleteEditorValue): State<Unit> {
        return try {
            SuccessState(mUserDao.deleteEditor(deleteEditorValue.editorId, deleteEditorValue.shoppingListId))
        }catch (e: Exception){
            ErrorState(RoomError(e))
        }
    }

    override suspend fun deleteEditorRemote(deleteEditorValue: DeleteEditorValue): State<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}