package com.aleksej.makaji.listopia.data.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.aleksej.makaji.listopia.data.room.model.ShoppingList
import com.aleksej.makaji.listopia.data.room.model.ShoppingListUserXRef
import com.aleksej.makaji.listopia.data.room.model.ShoppingListWithEditors

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShoppingList(shoppingList: ShoppingList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShoppingLists(shoppingLists: List<ShoppingList>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShoppingListsWithEditors(shoppingListWithEditors: List<ShoppingListUserXRef>): List<Long>

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList): Int

    @Query("SELECT * FROM shopping_list WHERE id = :id")
    fun getShoppingListById(id: String): LiveData<ShoppingListWithEditors>

    @Transaction @Query("SELECT * FROM shopping_list WHERE id = :shoppingListId")
    suspend fun getShoppingListByIdSuspend(shoppingListId: String): ShoppingListWithEditors?

    @Query("DELETE FROM shopping_list")
    suspend fun deleteAllShoppingLists(): Int

    @Query("DELETE FROM shopping_list WHERE id = :id")
    suspend fun deleteShoppingListById(id: String): Int

    @Query("DELETE FROM editors WHERE shoppingListId = :id")
    suspend fun deleteShoppingListsWithEditorsById(id: String): Int

    @Query("UPDATE shopping_list SET isSynced = 1 WHERE id = :shoppingListId")
    suspend fun updateSyncShoppingList(shoppingListId: String):  Int

    @Query("UPDATE shopping_list SET isSynced = 1 WHERE id IN (:shoppingListIds)")
    suspend fun updateSyncShoppingLists(shoppingListIds: List<String>):  Int

    //@Transaction @Query("SELECT * FROM shopping_list INNER JOIN editors ON shopping_list.id = editors.shoppingListId WHERE isDeleted = 0 ")
    @Transaction @Query("SELECT * FROM shopping_list WHERE isDeleted = 0")
    fun getShoppingListsWithEditors(): DataSource.Factory<Int, ShoppingListWithEditors>

    @Transaction @Query("SELECT * FROM shopping_list")
    suspend fun getShoppingListsSuspend(): List<ShoppingListWithEditors>

    @Transaction @Query("SELECT * FROM shopping_list WHERE shopping_list.isSynced = 0")
    suspend fun getShoppingListsNotSyncedSuspend(): List<ShoppingListWithEditors>
}