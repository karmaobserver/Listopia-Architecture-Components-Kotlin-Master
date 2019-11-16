package com.aleksej.makaji.listopia.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aleksej.makaji.listopia.data.room.dao.ProductDao
import com.aleksej.makaji.listopia.data.room.dao.ShoppingListDao
import com.aleksej.makaji.listopia.data.room.dao.UserDao
import com.aleksej.makaji.listopia.data.room.model.*

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Database(entities = [ShoppingList::class, Product::class, User::class, UserUserXRef::class, ShoppingListUserXRef::class], version = 26, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ListopiaDatabase : RoomDatabase() {
    abstract fun shoppingListDao() : ShoppingListDao
    abstract fun productDao() : ProductDao
    abstract fun userDao() : UserDao
}