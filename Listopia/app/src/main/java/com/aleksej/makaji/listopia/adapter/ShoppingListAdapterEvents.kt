package com.aleksej.makaji.listopia.adapter

import android.view.View

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
sealed class ShoppingListAdapterEvents {
    data class ShoppingListClick(val shoppingListId: Long, val shoppingListName: String) : ShoppingListAdapterEvents()
    data class OptionsClick(val view: View, val shoppingListId: Long) : ShoppingListAdapterEvents()
}