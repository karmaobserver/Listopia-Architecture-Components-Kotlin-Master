package com.aleksej.makaji.listopia.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Created by Aleksej Makaji on 2019-10-11.
 */
@Entity(tableName = "editors",
        primaryKeys = ["shoppingListId", "editorId"])
data class ShoppingListUserXRef(
        @ColumnInfo(index = true)
        val shoppingListId: String,
        @ColumnInfo(index = true)
        val editorId: String)