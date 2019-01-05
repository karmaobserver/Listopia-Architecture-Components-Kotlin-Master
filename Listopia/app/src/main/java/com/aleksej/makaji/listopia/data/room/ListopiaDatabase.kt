package com.aleksej.makaji.listopia.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aleksej.makaji.listopia.data.room.Converters
import com.aleksej.makaji.listopia.data.room.ShoppingListDao
import com.aleksej.makaji.listopia.data.room.model.ShoppingList

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Database(entities = [ShoppingList::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ListopiaDatabase : RoomDatabase() {
    abstract fun shoppingListDao() : ShoppingListDao
}