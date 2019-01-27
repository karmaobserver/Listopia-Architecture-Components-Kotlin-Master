package com.aleksej.makaji.listopia.data.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aleksej.makaji.listopia.data.room.model.ShoppingList

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveShoppingList(shoppingList: ShoppingList): Long

    @Query("SELECT * FROM shopping_list")
    fun getShoppingLists(): DataSource.Factory<Int, ShoppingList>

    @Query("SELECT * FROM shopping_list")
    fun getShoppingList(): LiveData<ShoppingList>

    @Query("DELETE FROM shopping_list")
    fun deleteAllShoppingLists(): Int
}