package com.aleksej.makaji.listopia.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Entity(tableName = "shopping_list")
data class ShoppingList(
        @PrimaryKey
        @ColumnInfo(index = true)
        val id: String,
        val name: String,
        val ownerId: String,
        val isDeleted: Boolean,
        val isSynced: Boolean,
        val timestamp: Date
)