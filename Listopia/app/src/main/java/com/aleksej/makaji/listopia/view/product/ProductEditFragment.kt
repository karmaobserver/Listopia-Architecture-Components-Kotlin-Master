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
import com.aleksej.makaji.listopia.data.usecase.value.ProductValue
import com.aleksej.makaji.listopia.databinding.FragmentProductEditBinding
import com.aleksej.makaji.listopia.error.ProductNameError
import com.aleksej.makaji.listopia.util.*
import com.aleksej.makaji.listopia.viewmodel.ProductViewModel

/**
 * Created by Aleksej Makaji on 4/27/19.
 */
class ProductEditFragment: BaseFragment() {

    private lateinit var mProductViewModel: ProductViewModel

    private var binding by autoCleared<FragmentProductEditBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private var mProductId: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentProductEditBinding>(
                inflater,
                R.layout.fragment_product_edit,
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
        initObservers()
        showKeyboard()
    }

    private fun initData() {
        binding.productViewModel = mProductViewModel
        arguments?.let {
            mProductId = ProductEditFragmentArgs.fromBundle(it).productId
            if (mProductViewModel.reloadEditData) {
                mProductViewModel.reloadEditData = true
                mProductId?.run {
                    mProductViewModel.getProductById(ProductValue(this))

                }
            }
        }
    }

    private fun initObservers() {
        observeEditProduct()
        observeGetProductById()
    }

    private fun observeEditProduct() {
        observeSingle(mProductViewModel.updateProductLiveData) {
            binding.state = it
            when (it) {
                is State.Success -> {
                    hideKeyboard()
                    showToastLong(R.string.success_product_edit)
                    findNavController().navigateUp()
                }
                is State.Error -> {
                    hideKeyboard()
                    when (it.error) {
                        is ProductNameError -> binding.textInputLayoutProductName.error = getString(it.error.resourceId)
                        else -> showError(it.error)
                    }
                }
            }
        }
    }

    private fun observeGetProductById() {
        observeSingle(mProductViewModel.getProductByIdLiveData) {
            binding.state = it
            when (it) {
                is State.Success -> {
                    binding.productModel = it.data
                    binding.executePendingBindings()
                }
                is State.Error -> {
                    showError(it.error)
                }
            }
        }
    }
}