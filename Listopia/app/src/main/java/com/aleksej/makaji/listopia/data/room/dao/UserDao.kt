package com.aleksej.makaji.listopia.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aleksej.makaji.listopia.data.room.model.User
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

    @Query("SELECT * FROM user")
    fun getUser(): LiveData<User?>

    @Transaction @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserWithFriends(userId: String): LiveData<UserWithFriends?>

    @Transaction @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserWithFriendsSuspended(userId: String): UserWithFriends
}