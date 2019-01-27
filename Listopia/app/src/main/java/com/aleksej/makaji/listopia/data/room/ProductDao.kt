package com.aleksej.makaji.listopia.data.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aleksej.makaji.listopia.data.room.model.Product

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProduct(product: Product): Long

    @Query("SELECT * FROM product WHERE shoppingListId = :shoppingListId")
    fun getProductsByShoppingListId(shoppingListId: Long): DataSource.Factory<Int, Product>

    @Query("DELETE FROM product WHERE shoppingListId = :shoppingListId")
    fun deleteProductsByShoppingList(shoppingListId: Long): Int
}