package com.aleksej.makaji.listopia.view.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.binding.FragmentDataBindingComponent
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.usecase.value.SaveProductValue
import com.aleksej.makaji.listopia.databinding.FragmentProductAddBinding
import com.aleksej.makaji.listopia.error.ListNameError
import com.aleksej.makaji.listopia.error.ProductNameError
import com.aleksej.makaji.listopia.util.*
import com.aleksej.makaji.listopia.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_product_add.*
import java.util.*

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class ProductAddFragment: BaseFragment() {

    private lateinit var mProductViewModel: ProductViewModel

    private var binding by autoCleared<FragmentProductAddBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private var mShoppingListId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentProductAddBinding>(
                inflater,
                R.layout.fragment_product_add,
                container,
                false,
                mDataBindingComponent
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mProductViewModel = viewModel(mViewModelFactory)
        initData()
        initListeners()
        initObservers()
        showKeyboard()
    }

    private fun initData() {
        arguments?.let {
            mShoppingListId = ProductAddFragmentArgs.fromBundle(it).shoppingListId
        }
    }

    private fun initObservers() {
        observeSaveShoppingList()
    }

    private fun observeSaveShoppingList() {
        observeSingle(mProductViewModel.addProductLiveData, {
            hideKeyboard()
            showToastLong(R.string.success_product_add)
            findNavController().navigateUp()
        }, onError = {
            when (it) {
                is ProductNameError -> binding.textInputLayoutProductName.error = getString(it.resourceId)
                else -> showError(it)
            }
        }, onLoading = {
            showLoading()
            button_add_product.isEnabled = false
        }, onHideLoading = {
            button_add_product.isEnabled = true
            hideLoading()
        })
    }

    private fun initListeners() {
        button_add_product.setOnClickListener {
            addProduct()
        }
    }

    private fun addProduct() {
        mShoppingListId?.let {
            mProductViewModel.addProduct(SaveProductValue(UUID.randomUUID().toString(),
                    binding.editTextProductName.text(),
                    binding.editTextQuantity.textDouble(),
                    binding.editTextUnit.text(),
                    binding.editTextPrice.textDouble(),
                    binding.editTextNotes.text(),
                    it))
        }
    }
}