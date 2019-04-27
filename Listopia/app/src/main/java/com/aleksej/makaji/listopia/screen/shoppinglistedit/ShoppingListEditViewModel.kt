package com.aleksej.makaji.listopia.screen.shoppinglistedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.mapper.ModelToValueMapper
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.GetShoppingListByIdUseCase
import com.aleksej.makaji.listopia.data.usecase.UpdateShoppingListUseCase
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/27/19.
 */
class ShoppingListEditViewModel @Inject constructor(private val mUpdateShoppingListUseCase: UpdateShoppingListUseCase,
                                                    private val mGetShoppingListByIdUseCase: GetShoppingListByIdUseCase) : ViewModel() {

    var reloadEditData = true

    private val _updateShoppingList = MutableLiveData<ShoppingListValue>()
    val updateShoppingListLiveData = Transformations.switchMap(_updateShoppingList) {
        mUpdateShoppingListUseCase.invoke(it)
    }

    private val _getShoppingListById = MutableLiveData<ShoppingListByIdValue>()
    val getShoppingListByIdLiveData = Transformations.switchMap(_getShoppingListById) {
        mGetShoppingListByIdUseCase.invoke(it)
    }

    fun updateShoppingList(shoppingListModel: ShoppingListModel) {
        _updateShoppingList.postValue(ModelToValueMapper.mapShoppingList(shoppingListModel))
    }

    fun getShoppingListById(shoppingListByIdValue: ShoppingListByIdValue) {
        _getShoppingListById.postValue(shoppingListByIdValue)
    }
}