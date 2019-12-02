package com.aleksej.makaji.listopia.adapter

import android.view.View
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
sealed class ShoppingListAdapterEvents {
    data class ShoppingListClick(val shoppingListId: String, val shoppingListName: String) : ShoppingListAdapterEvents()
    data class OptionsClick(val view: View, val shoppingListModel: ShoppingListModel) : ShoppingListAdapterEvents()
}