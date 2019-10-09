package com.aleksej.makaji.listopia.data.room.dao

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
    fun getShoppingListById(id: String): LiveData<ShoppingList>

    @Query("SELECT * FROM shopping_list WHERE id = :id")
    suspend fun getShoppingListByIdSuspend(id: String): ShoppingList

    @Query("DELETE FROM shopping_list")
    suspend fun deleteAllShoppingLists(): Int

    @Query("DELETE FROM shopping_list WHERE id = :id")
    suspend fun deleteShoppingListById(id: String): Int

    @Query("UPDATE shopping_list SET isSynced = 1 WHERE id = :shoppingListId")
    suspend fun updateSyncShoppingList(shoppingListId: String):  Int
}