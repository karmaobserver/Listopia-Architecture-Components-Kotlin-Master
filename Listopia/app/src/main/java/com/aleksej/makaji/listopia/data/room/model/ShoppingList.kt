package com.aleksej.makaji.listopia.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Entity(tableName = "shopping_list")
data class ShoppingList(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val ownerUid: String
)