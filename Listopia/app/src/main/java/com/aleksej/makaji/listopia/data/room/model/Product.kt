package com.aleksej.makaji.listopia.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
@Entity(tableName = "product")
data class Product(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val name: String,
        val quantity: Double,
        val unit: String,
        val price: Double,
        val notes: String,
        val isChecked: Boolean,
        val shoppingListId: Long
)