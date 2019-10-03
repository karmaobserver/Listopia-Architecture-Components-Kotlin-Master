package com.aleksej.makaji.listopia.data.room.model

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Created by Aleksej Makaji on 2019-06-02.
 */
@Entity(tableName = "join_user_shopping_list",
        primaryKeys = ["userId", "shoppingListId"],
        foreignKeys = [
            ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId")),
            ForeignKey(entity = ShoppingList::class, parentColumns = arrayOf("id"), childColumns = arrayOf("shoppingListId"))
        ])
data class JoinUserShoppingList(val userId: Long,
                                val shoppingListId: Long)