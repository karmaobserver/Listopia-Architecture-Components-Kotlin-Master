package com.aleksej.makaji.listopia.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aleksej.makaji.listopia.data.room.model.JoinUserShoppingList
import com.aleksej.makaji.listopia.data.room.model.Product
import com.aleksej.makaji.listopia.data.room.model.ShoppingList
import com.aleksej.makaji.listopia.data.room.model.User

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Database(entities = [ShoppingList::class, Product::class, User::class, JoinUserShoppingList::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ListopiaDatabase : RoomDatabase() {
    abstract fun shoppingListDao() : ShoppingListDao
    abstract fun productDao() : ProductDao
    abstract fun userDao() : UserDao
    abstract fun joinUserShoppingListDao() : JoinUserShoppingListDao
}