package com.aleksej.makaji.listopia.screen.shoppinglistedit

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

/**
 * Created by Aleksej Makaji on 1/27/19.
 */

class ShoppingListEditFragment: BaseFragment() {

    private lateinit var mShoppingListEditViewModel: ShoppingListEditViewModel

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
        mShoppingListEditViewModel = viewModel(mViewModelFactory)
        initData()
        initObservers()
        showKeyboard()
    }

    private fun initData() {
        binding.shoppingListEditViewModel = mShoppingListEditViewModel
        arguments?.let {
            mShoppingListId = ShoppingListEditFragmentArgs.fromBundle(it).shoppingListId
            if (mShoppingListEditViewModel.reloadEditData) {
                mShoppingListEditViewModel.reloadEditData = true
                mShoppingListId?.let {
                    mShoppingListEditViewModel.getShoppingListById(ShoppingListByIdValue(it))

                }
            }
        }
    }

    private fun initObservers() {
        observeEditShoppingList()
        observeGetShoppingListById()
    }

    private fun observeEditShoppingList() {
        observeSingle(mShoppingListEditViewModel.updateShoppingListLiveData) {
            binding.state = it
            when (it) {
                is State.Success -> {
                    hideKeyboard()
                    showToastLong(R.string.success_shopping_list_edit)
                    findNavController().navigateUp()
                }
                is State.Error -> {
                    when (it.error) {
                        is ListNameError -> binding.textInputLayoutListName.error = getString(R.string.error_list_name)
                        else -> showError(it.error)
                    }
                }
            }
        }
    }

    private fun observeGetShoppingListById() {
        observeSingle(mShoppingListEditViewModel.getShoppingListByIdLiveData) {
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