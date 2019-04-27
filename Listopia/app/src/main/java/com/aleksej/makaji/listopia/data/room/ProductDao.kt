package com.aleksej.makaji.listopia.data.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.aleksej.makaji.listopia.data.room.model.Product

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProduct(product: Product): Long

    @Update
    fun updateProduct(product: Product): Int

    @Query("SELECT * FROM product WHERE id = :productId")
    fun getProductById(productId: Long): LiveData<Product?>

    @Query("SELECT * FROM product WHERE shoppingListId = :shoppingListId")
    fun getProductsByShoppingListId(shoppingListId: Long): DataSource.Factory<Int, Product>

    @Query("DELETE FROM product WHERE shoppingListId = :shoppingListId")
    fun deleteProductsByShoppingList(shoppingListId: Long): Int

    @Query("DELETE FROM product WHERE id = :productId")
    fun deleteProductById(productId: Long): Int
}