package com.aleksej.makaji.listopia.adapter

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
sealed class ShoppingListAdapterEvents {
    data class ShoppingListClick(val shoppingListId: Long) : ShoppingListAdapterEvents()
}