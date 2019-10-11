package com.aleksej.makaji.listopia.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Created by Aleksej Makaji on 2019-10-08.
 */
@Entity(tableName = "friends",
        primaryKeys = ["userId", "friendId"])
data class UserUserXRef(
        @ColumnInfo(index = true)
        val userId: String,
        @ColumnInfo(index = true)
        val friendId: String)