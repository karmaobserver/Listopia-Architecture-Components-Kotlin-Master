package com.aleksej.makaji.listopia.screen.productlist

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.adapter.ProductAdapter
import com.aleksej.makaji.listopia.adapter.ProductAdapterEvents
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.binding.FragmentDataBindingComponent
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import com.aleksej.makaji.listopia.databinding.FragmentProductListBinding
import com.aleksej.makaji.listopia.util.*
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class ProductListFragment: BaseFragment() {

    @Inject
    lateinit var mSharedPreferenceManager: SharedPreferenceManager

    private lateinit var mProductListViewModel: ProductListViewModel

    private var binding by autoCleared<FragmentProductListBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var mProductAdapter by autoCleared<ProductAdapter>()

    private var mShoppingListId: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentProductListBinding>(
                inflater,
                R.layout.fragment_product_list,
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
        mProductListViewModel = viewModel(mViewModelFactory)
        binding.productViewModel = mProductListViewModel
        initData()
        initRecyclerView()
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_fragment_product_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.submenu_options_delete_all -> {
                showToastLong("DELETING ALL PRODUCTS")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initData() {
        arguments?.let {
            mShoppingListId = ProductListFragmentArgs.fromBundle(it).shoppingListId
            mShoppingListId?.let {
                mProductListViewModel.getProductsByShoppingId(ProductsValue(it))
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.recyclerViewProduct.layoutManager = layoutManager
        binding.recyclerViewProduct.setHasFixedSize(false)

        mProductAdapter = ProductAdapter(mDataBindingComponent, mSharedPreferenceManager) {
            when (it) {
                is ProductAdapterEvents.ProductClick -> {
                    mProductListViewModel.updateProduct(it.productModel)
                }
                is ProductAdapterEvents.EditClick -> {}
            }
        }
        binding.recyclerViewProduct.adapter = mProductAdapter
    }

    private fun initObservers() {
        observeProducts()
        observeAddProduct()
        observeUpdateProduct()
    }

    private fun observeUpdateProduct() {
        observeSingle(mProductListViewModel.updateProductLiveData) {
            when (it) {
                is State.Error -> showError(it.error)
            }
        }
    }

    private fun observeAddProduct() {
        observeSingle(mProductListViewModel.addProductEvent) {
            mShoppingListId?.let {
                findNavController().navigate(ProductListFragmentDirections.actionFragmentProductListToFragmentProductAdd(it))
            }
        }
    }

    private fun observeProducts() {
        observePeek(mProductListViewModel.productsByShoppingIdLiveData) {
            binding.state = it
            when (it) {
                is State.Success -> {
                    it.data?.let {
                        mProductAdapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun setupOptionsPopupMenu(view: View, shoppingListId: Long) {
        context?.let {
            val popup = PopupMenu(it, view)
            popup.inflate(R.menu.popup_menu_shopping_list)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_menu_edit_product_list -> {
                        findNavController().navigate(ShoppingListFragmentDirections.actionFragmentShoppingListToFragmentShoppingListEdit(shoppingListId))
                    }
                    R.id.popup_menu_delete_product_list -> {
                        mShoppingListViewModel.deleteShoppingListById(DeleteShoppingListValue(shoppingListId))
                    }
                }
                false
            }
            popup.show()
        }
    }
}