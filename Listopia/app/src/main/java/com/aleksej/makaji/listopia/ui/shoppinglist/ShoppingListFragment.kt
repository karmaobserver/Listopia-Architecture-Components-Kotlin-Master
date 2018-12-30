package com.aleksej.makaji.listopia.ui.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.data.event.ErrorEvent
import com.aleksej.makaji.listopia.data.event.LoadingEvent
import com.aleksej.makaji.listopia.data.event.SuccessEvent
import com.aleksej.makaji.listopia.util.observePeek
import com.aleksej.makaji.listopia.util.viewModel
import timber.log.Timber

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListFragment: BaseFragment() {

    private lateinit var mShoppingListViewModel: ShoppingListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mShoppingListViewModel = viewModel(mViewModelFactory)
        initObservers()

        showToast(mShoppingListViewModel.test)
    }

    private fun initObservers() {
        observeShoppingLists()
    }

    private fun observeShoppingLists() {
        observePeek(mShoppingListViewModel.shoppingListsEvent) {
            when (it) {
                is SuccessEvent -> {
                    it.data?.let {
                        Timber.d("Size is: ${it.size}")
                    }
                }
                is LoadingEvent -> {}
                is ErrorEvent -> {
                    showError(it.error)
                }
            }
        }
    }
}