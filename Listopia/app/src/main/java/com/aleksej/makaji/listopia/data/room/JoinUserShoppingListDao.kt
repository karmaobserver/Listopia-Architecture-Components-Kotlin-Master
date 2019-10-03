package com.aleksej.makaji.listopia.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.aleksej.makaji.listopia.data.room.model.ShoppingList
import com.aleksej.makaji.listopia.data.room.model.User

/**
 * Created by Aleksej Makaji on 2019-06-02.
 */
@Dao
interface JoinUserShoppingListDao {

    @Query("SELECT * FROM user INNER JOIN join_user_shopping_list ON user.id = join_user_shopping_list.userId WHERE join_user_shopping_list.shoppingListId =:shoppingListId")
    fun getEditorsForShoppingList(shoppingListId: Long): LiveData<List<User>>

    @Query("SELECT * FROM shopping_list INNER JOIN join_user_shopping_list ON shopping_list.id = join_user_shopping_list.shoppingListId WHERE join_user_shopping_list.userId =:userId")
    fun getShoppingListForUser(userId: Long): LiveData<List<ShoppingList>>
}