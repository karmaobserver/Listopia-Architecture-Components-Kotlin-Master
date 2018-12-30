package com.aleksej.makaji.listopia.ui.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.util.viewModel

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

        showToast(mShoppingListViewModel.test)
    }
}