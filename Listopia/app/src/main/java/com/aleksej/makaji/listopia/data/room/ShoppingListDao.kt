package com.aleksej.makaji.listopia.data.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.aleksej.makaji.listopia.data.room.model.ShoppingList

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShoppingList(shoppingList: ShoppingList): Long

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList): Int

    @Query("SELECT * FROM shopping_list")
    fun getShoppingLists(): DataSource.Factory<Int, ShoppingList>

    @Query("SELECT * FROM shopping_list WHERE id = :id")
    fun getShoppingListById(id: Long): LiveData<ShoppingList>

    @Query("DELETE FROM shopping_list")
    suspend fun deleteAllShoppingLists(): Int

    @Query("DELETE FROM shopping_list WHERE id = :id")
    suspend fun deleteShoppingListById(id: Long): Int
}