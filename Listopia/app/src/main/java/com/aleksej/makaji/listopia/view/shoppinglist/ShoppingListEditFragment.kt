package com.aleksej.makaji.listopia.view.shoppinglist

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
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListByIdValue
import com.aleksej.makaji.listopia.databinding.FragmentShoppingListEditBinding
import com.aleksej.makaji.listopia.error.ListNameError
import com.aleksej.makaji.listopia.util.*
import com.aleksej.makaji.listopia.viewmodel.ShoppingListViewModel

/**
 * Created by Aleksej Makaji on 1/27/19.
 */

class ShoppingListEditFragment: BaseFragment() {

    private lateinit var mShoppingListViewModel: ShoppingListViewModel

    private var binding by autoCleared<FragmentShoppingListEditBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private var mShoppingListId: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentShoppingListEditBinding>(
                inflater,
                R.layout.fragment_shopping_list_edit,
                container,
                false,
                mDataBindingComponent
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mShoppingListViewModel = viewModel(mViewModelFactory)
        initData()
        initObservers()
        showKeyboard()
    }

    private fun initData() {
        binding.shoppingListViewModel = mShoppingListViewModel
        arguments?.let {
            mShoppingListId = ShoppingListEditFragmentArgs.fromBundle(it).shoppingListId
            if (mShoppingListViewModel.reloadEditData) {
                mShoppingListViewModel.reloadEditData = true
                mShoppingListId?.let {
                    mShoppingListViewModel.getShoppingListById(ShoppingListByIdValue(it))

                }
            }
        }
    }

    private fun initObservers() {
        observeEditShoppingList()
        observeGetShoppingListById()
    }

    private fun observeEditShoppingList() {
        observeSingle(mShoppingListViewModel.updateShoppingListLiveData) {
            binding.state = it
            when (it) {
                is State.Success -> {
                    hideKeyboard()
                    showToastLong(R.string.success_shopping_list_edit)
                    findNavController().navigateUp()
                }
                is State.Error -> {
                    when (it.error) {
                        is ListNameError -> binding.textInputLayoutListName.error = getString(it.error.resourceId)
                        else -> showError(it.error)
                    }
                }
            }
        }
    }

    private fun observeGetShoppingListById() {
        observeSingle(mShoppingListViewModel.getShoppingListByIdLiveData) {
            binding.state = it
            when (it) {
                is State.Success -> {
                    binding.shoppingListModel = it.data
                    binding.executePendingBindings()
                }
                is State.Error -> {
                    showError(it.error)
                }
            }
        }
    }
}