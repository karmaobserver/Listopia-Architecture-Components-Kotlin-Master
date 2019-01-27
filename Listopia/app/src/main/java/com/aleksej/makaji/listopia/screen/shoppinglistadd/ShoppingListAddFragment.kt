package com.aleksej.makaji.listopia.screen.shoppinglistadd

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
import com.aleksej.makaji.listopia.data.usecase.value.ShoppingListValue
import com.aleksej.makaji.listopia.databinding.FragmentShoppingListAddBinding
import com.aleksej.makaji.listopia.error.ListNameError
import com.aleksej.makaji.listopia.util.*
import kotlinx.android.synthetic.main.fragment_shopping_list_add.*
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/13/19.
 */
class ShoppingListAddFragment: BaseFragment() {

    @Inject
    lateinit var mSharedPreferenceManager: SharedPreferenceManager

    private lateinit var mShoppingListAddViewModel: ShoppingListAddViewModel

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
        mShoppingListAddViewModel = viewModel(mViewModelFactory)
        initListeners()
        initObservers()
        showKeyboard()
    }

    private fun initObservers() {
        observeSaveShoppingList()
    }

    private fun observeSaveShoppingList() {
        observeSingle(mShoppingListAddViewModel.saveShoppingListLiveData) {
            binding.state = it
            when(it) {
                is State.Success -> {
                    showToastLong(R.string.success_create_shopping_list)
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

    private fun initListeners() {
        button_create_list.setOnClickListener {
            createShoppingList()
            hideKeyboard()
        }
        edit_text_list_name.onSubmit {
            createShoppingList()
            hideKeyboard()
        }
    }

    private fun createShoppingList() {
        mShoppingListAddViewModel.createShoppingList(ShoppingListValue(binding.editTextListName.text(), mSharedPreferenceManager.userUid))
    }
}