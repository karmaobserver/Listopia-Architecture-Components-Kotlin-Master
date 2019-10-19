package com.aleksej.makaji.listopia.data.room.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Created by Aleksej Makaji on 2019-10-11.
 */
data class ShoppingListWithEditors(
        @Embedded
        val shoppingList: ShoppingList,

        @Relation(
                parentColumn = "id",
                entity = User::class,
                entityColumn = "id",
                associateBy = Junction(
                        value = ShoppingListUserXRef::class,
                        parentColumn = "shoppingListId",
                        entityColumn = "editorId")
        )
        val editors: List<User>
)