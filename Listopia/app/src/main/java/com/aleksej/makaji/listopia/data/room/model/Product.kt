package com.aleksej.makaji.listopia.data.room.model

import androidx.room.*
import java.util.*

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
@Entity(tableName = "product",
        foreignKeys = [ForeignKey(entity = ShoppingList::class, parentColumns = arrayOf("id"), childColumns = arrayOf("shoppingListId"))])
data class Product(
        @PrimaryKey
        @ColumnInfo(index = true)
        val id: String,
        val name: String,
        val quantity: Double,
        val unit: String,
        val price: Double,
        val notes: String,
        val isDeleted: Boolean,
        val isChecked: Boolean,
        @ColumnInfo(index = true)
        val shoppingListId: String,
        val isSynced: Boolean,
        val timestamp: Date
)