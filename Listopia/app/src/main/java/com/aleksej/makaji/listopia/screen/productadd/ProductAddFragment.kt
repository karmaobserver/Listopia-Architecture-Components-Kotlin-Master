package com.aleksej.makaji.listopia.screen.productadd

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
import com.aleksej.makaji.listopia.util.*
import kotlinx.android.synthetic.main.fragment_product_add.*

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class ProductAddFragment: BaseFragment() {

    private lateinit var mProductAddViewModel: ProductAddViewModel

    private var binding by autoCleared<FragmentProductAddBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private var mShoppingListId: Long? = null

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
        mProductAddViewModel = viewModel(mViewModelFactory)
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
        observeSingle(mProductAddViewModel.addProductLiveData) {
            binding.state = it
            when(it) {
                is State.Success -> {
                    showToastLong(R.string.success_add_product)
                    findNavController().navigateUp()
                }
                is State.Error -> {
                    showError(it.error)
                }
            }
        }
    }

    private fun initListeners() {
        button_add_product.setOnClickListener {
            addProduct()
        }
    }

    private fun addProduct() {
        mShoppingListId?.let {
            mProductAddViewModel.addProduct(SaveProductValue(binding.editTextProductName.text(),
                    binding.editTextQuantity.textDouble(),
                    binding.editTextUnit.text(),
                    binding.editTextPrice.textDouble(),
                    binding.editTextNotes.text(),
                    it))
        }
        hideKeyboard()
    }
}