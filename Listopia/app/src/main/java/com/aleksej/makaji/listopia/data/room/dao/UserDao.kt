package com.aleksej.makaji.listopia.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aleksej.makaji.listopia.data.room.model.ShoppingListUserXRef
import com.aleksej.makaji.listopia.data.room.model.User
import com.aleksej.makaji.listopia.data.room.model.UserUserXRef
import com.aleksej.makaji.listopia.data.room.model.UserWithFriends

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsers(users: List<User>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFriend(friend: UserUserXRef): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFriends(friends: List<UserUserXRef>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEditor(editor: ShoppingListUserXRef): Long

    @Query("SELECT * FROM user")
    fun getUser(): LiveData<User?>

    @Transaction @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserWithFriends(userId: String): LiveData<UserWithFriends?>

    @Transaction @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserWithFriendsSuspended(userId: String): UserWithFriends

    @Query("DELETE FROM user WHERE id = :userId")
    suspend fun deleteUserById(userId: String)

    @Query("DELETE FROM friends WHERE userId = :userId AND friendId = :friendId")
    suspend fun deleteFriend(userId: String, friendId: String)

    @Query("DELETE FROM editors WHERE editorId = :editorId AND shoppingListId = :shoppingListId")
    suspend fun deleteEditor(editorId: String, shoppingListId: String)
}