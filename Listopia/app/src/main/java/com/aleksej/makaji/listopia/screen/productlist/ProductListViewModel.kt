package com.aleksej.makaji.listopia.screen.productlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.usecase.GetProductsByShoppingListUseCase
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class ProductListViewModel @Inject constructor(private val mGetProductsByShoppingListUseCase: GetProductsByShoppingListUseCase) : ViewModel() {

    private val _productsByShoppingId = MutableLiveData<ProductsValue>()
    val productsByShoppingIdLiveData = Transformations.switchMap(_productsByShoppingId) {
        mGetProductsByShoppingListUseCase.invoke(it)
    }

    private val _addProductEvent = MutableLiveData<StateHandler<Unit>>()
    val addProductEvent : LiveData<StateHandler<Unit>>
        get() = _addProductEvent

    fun addProductEvent() {
        _addProductEvent.postValue(StateHandler.success(Unit))
    }

    fun getProductsByShoppingId(productsValue: ProductsValue) {
        _productsByShoppingId.postValue(productsValue)
    }
}