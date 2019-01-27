package com.aleksej.makaji.listopia.screen.productadd

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.usecase.SaveProductUseCase
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class ProductAddViewModel @Inject constructor(private val mSaveProductUseCase: SaveProductUseCase) : ViewModel() {

    private val _addProduct = MutableLiveData<SaveProductValue>()
    val addProductLiveData = Transformations.switchMap(_addProduct) {
        mSaveProductUseCase.invoke(it)
    }

    fun addProduct(saveProductValue: SaveProductValue) {
        _addProduct.postValue(saveProductValue)
    }
}