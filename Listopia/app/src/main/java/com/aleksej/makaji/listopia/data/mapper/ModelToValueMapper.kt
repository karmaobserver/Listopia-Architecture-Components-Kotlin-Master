package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import com.aleksej.makaji.listopia.util.mapTo

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
fun ShoppingListModel.mapToShoppingListValue(): SaveShoppingListValue = mapTo<SaveShoppingListValue>().copy()