package com.aleksej.makaji.listopia.ui.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.binding.FragmentDataBindingComponent
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.databinding.FragmentShoppingListBinding
import com.aleksej.makaji.listopia.util.autoCleared
import com.aleksej.makaji.listopia.util.observePeek
import com.aleksej.makaji.listopia.util.viewModel
import timber.log.Timber

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListFragment: BaseFragment() {

    private lateinit var mShoppingListViewModel: ShoppingListViewModel

    private var binding by autoCleared<FragmentShoppingListBinding>()
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentShoppingListBinding>(
                inflater,
                R.layout.fragment_shopping_list,
                container,
                false,
                dataBindingComponent
        )
        binding = dataBinding
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mShoppingListViewModel = viewModel(mViewModelFactory)
        initObservers()

        /*binding.setLifecycleOwner(viewLifecycleOwner)
        binding.shopLiveData = mShoppingListViewModel.shoppingListLiveData*/
    }

    private fun initObservers() {
        observeShoppingLists()
        observeShoppingList()
    }

    private fun observeShoppingLists() {
        observePeek(mShoppingListViewModel.shoppingListsLiveData) {
            when (it) {
                is State.Success -> {
                    it.data?.let {
                        Timber.d("Size is: ${it.size}")
                    }
                }
                is State.Error -> {
                    showError(it.error)
                }
            }
        }
    }

    private fun observeShoppingList() {
        observePeek(mShoppingListViewModel.shoppingListLiveData) {
            binding.stateShop = it
            when (it) {
                is State.Success -> {
                    it.data?.let {
                        binding.shoppingListModel = it
                    }
                }
                is State.Error -> {
                    showError(it.error)
                }
            }
        }
    }
}