package com.aleksej.makaji.listopia.data.room.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Created by Aleksej Makaji on 2019-10-08.
 */
data class UserWithFriends(
        @Embedded
        val user: User,

        @Relation(
                parentColumn = "id",
                entity = User::class,
                entityColumn = "id",
                associateBy = Junction(
                        value = UserUserXRef::class,
                        parentColumn = "userId",
                        entityColumn = "friendId")
        )
        val friends: List<User>
)
