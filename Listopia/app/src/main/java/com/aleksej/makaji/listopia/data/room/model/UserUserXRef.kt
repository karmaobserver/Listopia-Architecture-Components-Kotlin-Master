package com.aleksej.makaji.listopia.data.room.model

import androidx.room.Entity

/**
 * Created by Aleksej Makaji on 2019-10-08.
 */
@Entity(primaryKeys = ["userId", "friendId"])
data class UserUserXRef(val userId: String,
                        val friendId: String)