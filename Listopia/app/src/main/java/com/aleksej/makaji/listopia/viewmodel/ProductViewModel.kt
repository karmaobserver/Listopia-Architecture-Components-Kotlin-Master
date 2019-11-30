package com.aleksej.makaji.listopia.viewmodel

import androidx.lifecycle.*
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.data.repository.ProductRepository
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.DeleteProductByIdUseCase
import com.aleksej.makaji.listopia.data.usecase.FetchAndSaveProductsUseCase
import com.aleksej.makaji.listopia.data.usecase.SaveProductUseCase
import com.aleksej.makaji.listopia.data.usecase.UpdateProductUseCase
import com.aleksej.makaji.listopia.data.usecase.value.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/27/19.
 */
class ProductViewModel @Inject constructor(private val mUpdateProductUseCase: UpdateProductUseCase,
                                           private val mSaveProductUseCase: SaveProductUseCase,
                                           private val mDeleteProductByIdUseCase: DeleteProductByIdUseCase,
                                           private val mFetchAndSaveProductsUseCase: FetchAndSaveProductsUseCase,
                                           private val mProductRepository: ProductRepository) : ViewModel() {

    var reloadEditData = true

    private val getProductsByShoppingIdTrigger = MutableLiveData<ProductsValue>()
    val getProductsByShoppingIdLiveData = Transformations.switchMap(getProductsByShoppingIdTrigger) { mProductRepository.getProductsByShoppingListId(it) }

    private val updateProductTrigger = MutableLiveData<StateHandler<Int>>()
    val updateProductLiveData : LiveData<StateHandler<Int>> = updateProductTrigger

    private val addProductTrigger = MutableLiveData<StateHandler<Long>>()
    val addProductLiveData : MutableLiveData<StateHandler<Long>> = addProductTrigger

    private val getProductByIdTrigger = MutableLiveData<ProductValue>()
    val getProductByIdLiveData = Transformations.switchMap(getProductByIdTrigger) { mProductRepository.getProductById(it) }

    private val deleteProductByIdTrigger = MutableLiveData<StateHandler<Int>>()
    val deleteProductByIdLiveData : LiveData<StateHandler<Int>> = deleteProductByIdTrigger

    private val addProductEventTrigger = MutableLiveData<StateHandler<Unit>>()
    val addProductEvent : LiveData<StateHandler<Unit>> = addProductEventTrigger

    private val fetchProductsTrigger = MutableLiveData<StateHandler<Unit>>()
    val fetchProductsLiveData : LiveData<StateHandler<Unit>> = fetchProductsTrigger

    fun getProductsByShoppingId(productsValue: ProductsValue) {
        getProductsByShoppingIdTrigger.postValue(productsValue)
    }

    fun getProductById(prodcutValue: ProductValue) {
        getProductByIdTrigger.postValue(prodcutValue)
    }

    fun fetchProducts(shoppingListsId: List<String>) {
        viewModelScope.launch {
            fetchProductsTrigger.value = StateHandler(mFetchAndSaveProductsUseCase.invoke(FetchProductsValue(shoppingListsId)))
        }
    }

    fun addProduct(saveProductValue: SaveProductValue) {
        viewModelScope.launch {
            addProductTrigger.value = StateHandler(mSaveProductUseCase.invoke(saveProductValue))
        }
    }

    fun addProductEvent() {
        addProductEventTrigger.postValue(StateHandler.success(Unit))
    }

    fun updateProduct(productModel: ProductModel) {
        viewModelScope.launch {
            updateProductTrigger.value = StateHandler(mUpdateProductUseCase.invoke(productModel))
        }
    }

    fun deleteProductById(deleteProductValue: DeleteProductValue) {
        viewModelScope.launch {
            deleteProductByIdTrigger.value = StateHandler(mDeleteProductByIdUseCase.invoke(deleteProductValue))
        }
    }
}