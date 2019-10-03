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
import com.aleksej.makaji.listopia.data.usecase.value.SaveShoppingListValue
import com.aleksej.makaji.listopia.databinding.FragmentShoppingListAddBinding
import com.aleksej.makaji.listopia.error.ListNameError
import com.aleksej.makaji.listopia.util.*
import com.aleksej.makaji.listopia.viewmodel.ShoppingListViewModel
import kotlinx.android.synthetic.main.fragment_shopping_list_add.*
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/13/19.
 */
class ShoppingListAddFragment: BaseFragment() {

    @Inject
    lateinit var mSharedPreferenceManager: SharedPreferenceManager

    private lateinit var mShoppingListViewModel: ShoppingListViewModel

    private var binding by autoCleared<FragmentShoppingListAddBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentShoppingListAddBinding>(
                inflater,
                R.layout.fragment_shopping_list_add,
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
        initListeners()
        initObservers()
        showKeyboard()
    }

    private fun initObservers() {
        observeSaveShoppingList()
    }

    private fun observeSaveShoppingList() {
        observeSingle(mShoppingListViewModel.saveShoppingListLiveData, {
            hideKeyboard()
            showToastLong(R.string.success_shopping_list_create)
            findNavController().navigateUp()
        }, onError = {
            when (it) {
                is ListNameError -> binding.textInputLayoutListName.error = getString(it.resourceId)
                else -> showError(it)
            }
        })
    }

    private fun initListeners() {
        button_create_list.setOnClickListener {
            createShoppingList()
        }
        edit_text_list_name.onSubmit {
            createShoppingList()
        }
    }

    private fun createShoppingList() {
        mShoppingListViewModel.createShoppingList(SaveShoppingListValue(binding.editTextListName.text(), mSharedPreferenceManager.userId))
    }
}