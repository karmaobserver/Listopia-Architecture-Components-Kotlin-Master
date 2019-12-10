package com.aleksej.makaji.listopia.view.product

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aleksej.makaji.listopia.HomeActivity
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.adapter.ProductAdapter
import com.aleksej.makaji.listopia.adapter.ProductAdapterEvents
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.binding.FragmentDataBindingComponent
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.usecase.value.DeleteProductValue
import com.aleksej.makaji.listopia.data.usecase.value.ProductsValue
import com.aleksej.makaji.listopia.databinding.FragmentProductListBinding
import com.aleksej.makaji.listopia.util.*
import com.aleksej.makaji.listopia.viewmodel.ProductViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.inject.Inject


/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class ProductListFragment: BaseFragment() {

    @Inject
    lateinit var mSharedPreferenceManager: SharedPreferenceManager

    private lateinit var mProductViewModel: ProductViewModel

    private var binding by autoCleared<FragmentProductListBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var mProductAdapter by autoCleared<ProductAdapter>()

    private var mShoppingListId: String? = null

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
        mProductViewModel = viewModel(mViewModelFactory)
        binding.productViewModel = mProductViewModel
        initData()
        initRecyclerView()
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_product_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.friends -> {
                mShoppingListId?.let {
                    findNavController().navigate(ProductListFragmentDirections.actionFragmentProductListToFriendListFragment(it))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initData() {
        arguments?.let {
            mShoppingListId = ProductListFragmentArgs.fromBundle(it).shoppingListId
            mShoppingListId?.let {
                mProductViewModel.getProductsByShoppingId(ProductsValue(it))
            }
            (activity as? HomeActivity)?.supportActionBar?.title = ProductListFragmentArgs.fromBundle(it).shoppingListName
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
                    it.productModel.isChecked = !it.productModel.isChecked
                    mProductViewModel.updateProduct(it.productModel)
                }
                is ProductAdapterEvents.OptionsClick -> {
                    setupOptionsPopupMenu(it.view, it.productModel)
                }
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
        observeSingle(mProductViewModel.updateProductLiveData, {
        }, onError = {
            showError(it)
        })
    }

    private fun observeAddProduct() {
        observeSingle(mProductViewModel.addProductEvent, {
            mShoppingListId?.let {
                findNavController().navigate(ProductListFragmentDirections.actionFragmentProductListToFragmentProductAdd(it))
            }
        })
    }

    private fun observeProducts() {
        observeSingle(mProductViewModel.getProductsByShoppingIdLiveData, {
            mProductAdapter.submitList(it)
            calculateTotalPriceList(it)
        }, onError = {
            showError(it)
        })
    }

    private fun setupOptionsPopupMenu(view: View, productModel: ProductModel) {
        context?.let {
            val popup = PopupMenu(it, view)
            popup.inflate(R.menu.popup_menu_product_list)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_menu_edit_product_list -> {
                        findNavController().navigate(ProductListFragmentDirections.actionFragmentProductListToFragmentProductEdit(productModel.id))
                    }
                    R.id.popup_menu_delete_product_list -> {
                        productModel.isDeleted = true
                        mProductViewModel.updateProduct(productModel)
                    }
                }
                false
            }
            popup.show()
        }
    }

    private fun calculateTotalPriceList(products: List<ProductModel>) {
        if (products.isNullOrEmpty()) {
            binding.textViewListTotal.text = "0"
            binding.textViewCartTotal.text = "0"
        } else {
            var listTotal = 0.0
            var cartTotal = 0.0
            products.forEach {
                if (it.quantity == 0.0 || it.quantity == null) {
                    listTotal += it.price
                    if (it.isChecked) {
                        cartTotal += it.price
                    }
                } else {
                    listTotal += it.price * it.quantity
                    if (it.isChecked) {
                        cartTotal += it.price * it.quantity
                    }
                }
            }

            binding.textViewListTotal.text = listTotal.toDecimalString()
            binding.textViewCartTotal.text = cartTotal.toDecimalString()
        }
    }
}