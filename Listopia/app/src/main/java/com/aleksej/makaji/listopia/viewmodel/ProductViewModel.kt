package com.aleksej.makaji.listopia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.*
import com.aleksej.makaji.listopia.data.usecase.value.*
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/27/19.
 */
class ProductViewModel @Inject constructor(private val mGetProductsByShoppingListUseCase: GetProductsByShoppingListUseCase,
                                           private val mUpdateProdctUseCase: UpdateProductUseCase,
                                           private val mSaveProductUseCase: SaveProductUseCase,
                                           private val mGetProductByIdUseCase: GetProductByIdUseCase,
                                           private val mDeleteProductByIdUseCase: DeleteProductByIdUseCase) : ViewModel() {

    var reloadEditData = true

    private val _productsByShoppingId = MutableLiveData<ProductsValue>()
    val productsByShoppingIdLiveData = Transformations.switchMap(_productsByShoppingId) {
        mGetProductsByShoppingListUseCase.invoke(it)
    }

    private val _updateProduct = MutableLiveData<ProductModel>()
    val updateProductLiveData = Transformations.switchMap(_updateProduct) {
        mUpdateProdctUseCase.invoke(it)
    }

    private val _addProductEvent = MutableLiveData<StateHandler<Unit>>()
    val addProductEvent : LiveData<StateHandler<Unit>>
        get() = _addProductEvent

    private val _addProduct = MutableLiveData<SaveProductValue>()
    val addProductLiveData = Transformations.switchMap(_addProduct) {
        mSaveProductUseCase.invoke(it)
    }

    private val _getProductById = MutableLiveData<ProductValue>()
    val getProductByIdLiveData = Transformations.switchMap(_getProductById) {
        mGetProductByIdUseCase.invoke(it)
    }

    private val _deleteProductById = MutableLiveData<ProductValue>()
    val deleteProductByIdLiveData = Transformations.switchMap(_deleteProductById) {
        mDeleteProductByIdUseCase.invoke(it)
    }

    fun getProductById(prodcutValue: ProductValue) {
        _getProductById.postValue(prodcutValue)
    }

    fun addProduct(saveProductValue: SaveProductValue) {
        _addProduct.postValue(saveProductValue)
    }

    fun addProductEvent() {
        _addProductEvent.postValue(StateHandler.success(Unit))
    }

    fun getProductsByShoppingId(productsValue: ProductsValue) {
        _productsByShoppingId.postValue(productsValue)
    }

    fun updateProduct(productModel: ProductModel) {
        _updateProduct.postValue(productModel)
    }

    fun deleteProductById(productModel: ProductValue) {
        _deleteProductById.postValue(productModel)
    }
}