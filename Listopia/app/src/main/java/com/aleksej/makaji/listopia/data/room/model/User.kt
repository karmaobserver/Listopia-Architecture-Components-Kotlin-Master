package com.aleksej.makaji.listopia.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
@Entity(tableName = "user")
data class User(
        @PrimaryKey
        val id: String = "",
        val name: String? = null,
        val email: String? = null,
        val avatar: String? = null)