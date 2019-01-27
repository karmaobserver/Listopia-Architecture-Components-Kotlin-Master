package com.aleksej.makaji.listopia.screen.shoppinglist

import android.os.Bundle
import android.view.*
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
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.databinding.FragmentShoppingListBinding
import com.aleksej.makaji.listopia.util.autoCleared
import com.aleksej.makaji.listopia.util.observePeek
import com.aleksej.makaji.listopia.util.observeSingle
import com.aleksej.makaji.listopia.util.viewModel

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class ShoppingListFragment: BaseFragment() {

    private lateinit var mShoppingListViewModel: ShoppingListViewModel

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
        binding.shoppingListViewModel = mShoppingListViewModel
        initRecyclerView()
        initObservers()

        /*binding.setLifecycleOwner(viewLifecycleOwner)
        binding.shopLiveData = mShoppingListViewModel.shoppingListLiveData*/
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_fragment_shopping_list, menu)
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

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.recyclerViewShoppingList.layoutManager = layoutManager
        binding.recyclerViewShoppingList.setHasFixedSize(false)

        mShoppingListAdapter = ShoppingListAdapter(mDataBindingComponent) {
            when (it) {
                is ShoppingListAdapterEvents.ShoppingListClick ->{
                    findNavController().navigate(ShoppingListFragmentDirections.actionFragmentShoppingListToFragmentProductList(it.shoppingListId))
                }
            }
        }
        binding.recyclerViewShoppingList.adapter = mShoppingListAdapter
    }

    private fun initObservers() {
        observeShoppingLists()
        observeAddShoppingList()
    }

    private fun observeShoppingLists() {
        observePeek(mShoppingListViewModel.shoppingListsLiveData) {
            binding.stateShop = it
            when (it) {
                is State.Success -> {
                    it.data?.let {
                        mShoppingListAdapter.submitList(it)
                    }
                }
                is State.Error -> {
                    showError(it.error)
                }
            }
        }
    }

    private fun observeAddShoppingList() {
        observeSingle(mShoppingListViewModel.addShoppingListEvent) {
            findNavController().navigate(R.id.action_fragment_shopping_list_to_fragment_shopping_list_add)
        }
    }
}