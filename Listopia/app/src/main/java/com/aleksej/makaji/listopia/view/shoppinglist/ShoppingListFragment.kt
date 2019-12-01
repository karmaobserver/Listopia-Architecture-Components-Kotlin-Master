package com.aleksej.makaji.listopia.view.shoppinglist

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.adapter.ShoppingListAdapter
import com.aleksej.makaji.listopia.adapter.ShoppingListAdapterEvents
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.binding.FragmentDataBindingComponent
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteShoppingListValue
import com.aleksej.makaji.listopia.databinding.FragmentShoppingListBinding
import com.aleksej.makaji.listopia.util.*
import com.aleksej.makaji.listopia.viewmodel.ProductViewModel
import com.aleksej.makaji.listopia.viewmodel.ShoppingListViewModel
import com.aleksej.makaji.listopia.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.item_shopping_list.view.*
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListFragment: BaseFragment() {

    @Inject
    lateinit var mSharedPreferenceManager: SharedPreferenceManager

    private lateinit var mShoppingListViewModel: ShoppingListViewModel
    private lateinit var mProductViewModel: ProductViewModel
    private lateinit var mUserViewModel: UserViewModel

    private var binding by autoCleared<FragmentShoppingListBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var mShoppingListAdapter by autoCleared<ShoppingListAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentShoppingListBinding>(
                inflater,
                R.layout.fragment_shopping_list,
                container,
                false,
                mDataBindingComponent
        )
        setHasOptionsMenu(true)
        binding = dataBinding
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mShoppingListViewModel = viewModel(mViewModelFactory)
        mProductViewModel = viewModel(mViewModelFactory)
        mUserViewModel = viewModel(mViewModelFactory)
        initRecyclerView()
        initObservers()
        initData()

        /*binding.setLifecycleOwner(viewLifecycleOwner)
        binding.shopLiveData = mShoppingListViewModel.shoppingListLiveData*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_shopping_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.submenu_options_delete_all -> {
                showToastLong("DELETE")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initData() {
        updateFirebaseToken()
        binding.shoppingListViewModel = mShoppingListViewModel
        if (mSharedPreferenceManager.userId != "") {
            mShoppingListViewModel.fetchShoppingListsByUserId(mSharedPreferenceManager.userId)
        }
        mShoppingListViewModel.getShoppingLists()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.recyclerViewShoppingList.layoutManager = layoutManager
        binding.recyclerViewShoppingList.setHasFixedSize(false)

        mShoppingListAdapter = ShoppingListAdapter(mDataBindingComponent) {
            when (it) {
                is ShoppingListAdapterEvents.ShoppingListClick ->{
                    findNavController().navigate(ShoppingListFragmentDirections.actionFragmentShoppingListToFragmentProductList(it.shoppingListId, it.shoppingListName))
                }
                is ShoppingListAdapterEvents.OptionsClick -> {
                    setupOptionsPopupMenu(it.view, it.shoppingListId)
                }
            }
        }
        binding.recyclerViewShoppingList.adapter = mShoppingListAdapter
    }

    private fun initObservers() {
        observeShoppingLists()
        observeAddShoppingList()
        observeDeleteShoppingListById()
        observeFetchAndSaveShoppingLists()
    }

    private fun observeFetchAndSaveShoppingLists() {
        observeSingle(mShoppingListViewModel.fetchShoppingListsLiveData, {
            mProductViewModel.fetchProducts(it)
        }, onError = {
            showError(it)
        })
    }

    private fun observeShoppingLists() {
        observePeek(mShoppingListViewModel.getShoppingListsLiveData, {
            mShoppingListAdapter.submitList(it)
        }, onError = {
            showError(it)
        })
    }

    private fun observeAddShoppingList() {
        observeSingle(mShoppingListViewModel.addShoppingListEvent, {
            findNavController().navigate(R.id.action_fragment_shopping_list_to_fragment_shopping_list_add)
        })
    }

    private fun observeDeleteShoppingListById() {
        observeSingle(mShoppingListViewModel.deleteShoppingListByIdLiveData, {
            showToastLong(R.string.success_shopping_list_delete)
        }, onError = {
            showError(it)
        })
    }

    private fun setupOptionsPopupMenu(view: View, shoppingListId: String) {
        context?.let {
            val popup = PopupMenu(it, view)
            popup.inflate(R.menu.popup_menu_shopping_list)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_menu_rename_shopping_list -> {
                        findNavController().navigate(ShoppingListFragmentDirections.actionFragmentShoppingListToFragmentShoppingListEdit(shoppingListId))
                    }
                    R.id.popup_menu_delete_shopping_list -> {
                        mShoppingListViewModel.deleteShoppingListById(DeleteShoppingListValue(shoppingListId))
                    }
                    R.id.popup_menu_share_shopping_list -> {}
                    R.id.popup_menu_copy_shopping_list -> {}
                }
                false
            }
            popup.show()
        }
    }

    private fun updateFirebaseToken() {
        mUserViewModel.updateFirebaseToken()
    }
}