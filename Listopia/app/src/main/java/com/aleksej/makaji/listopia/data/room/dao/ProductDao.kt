package com.aleksej.makaji.listopia.data.room.dao

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
    suspend fun saveProduct(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProducts(products: List<Product>): List<Long>

    @Update
    suspend fun updateProduct(product: Product): Int

    @Query("SELECT * FROM product WHERE id = :productId")
    fun getProductById(productId: String): LiveData<Product?>

    @Query("SELECT * FROM product WHERE shoppingListId = :shoppingListId")
    fun getProductsByShoppingListId(shoppingListId: String): DataSource.Factory<Int, Product>

    @Query("DELETE FROM product WHERE shoppingListId = :shoppingListId")
    suspend fun deleteProductsByShoppingList(shoppingListId: String): Int

    @Query("DELETE FROM product WHERE id = :productId")
    suspend fun deleteProductById(productId: String): Int

    @Query("SELECT * FROM product")
    suspend fun getProductsSuspend(): List<Product>

    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getProductByIdSuspend(productId: String): Product

    @Query("UPDATE product SET isSynced = 1 WHERE id = :productId")
    suspend fun updateSyncProduct(productId: String):  Int
}