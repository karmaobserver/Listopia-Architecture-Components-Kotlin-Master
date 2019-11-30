package com.aleksej.makaji.listopia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.databinding.ItemShoppingListBinding

/**
 * Created by Aleksej Makaji on 1/8/19.
 */
class ShoppingListAdapter(private val mDataBindingComponent: DataBindingComponent, private val mShoppingListAdapterEvents: (ShoppingListAdapterEvents) -> Unit) : DataBoundPagedListAdapter<ShoppingListModel, ItemShoppingListBinding>(
        diffCallback = object : DiffUtil.ItemCallback<ShoppingListModel>() {
            override fun areItemsTheSame(oldItem: ShoppingListModel, newItem: ShoppingListModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ShoppingListModel, newItem: ShoppingListModel): Boolean {
                return oldItem.name == newItem.name
                        && oldItem.ownerId == newItem.ownerId
                        && oldItem.isSynced == newItem.isSynced
                        && oldItem.timestamp == newItem.timestamp
                        && oldItem.editors == newItem.editors
            }
        }
) {

    override fun createBinding(parent: ViewGroup): ItemShoppingListBinding {
        val binding = DataBindingUtil.inflate<ItemShoppingListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_shopping_list,
                parent,
                false,
                mDataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.shoppingListModel?.let {
                mShoppingListAdapterEvents.invoke(ShoppingListAdapterEvents.ShoppingListClick(it.id, it.name))
            }
        }
        binding.imageButtonShoppingListOptions.setOnClickListener { view ->
            binding.shoppingListModel?.let {
                mShoppingListAdapterEvents.invoke(ShoppingListAdapterEvents.OptionsClick(view, it.id))
            }
        }
        return binding
    }

    override fun bind(binding: ItemShoppingListBinding, item: ShoppingListModel) {
        binding.shoppingListModel = item
        countCheckedProducts(binding, item)
    }

    private fun countCheckedProducts(binding: ItemShoppingListBinding, shoppingListModel: ShoppingListModel) {
        if (shoppingListModel.products.isNullOrEmpty()) {
            binding.textViewMaxProducts.text = "0"
            binding.textViewCheckedProducts.text = "0"
            return
        } else {
            binding.textViewMaxProducts.text = shoppingListModel.products.size.toString()
        }
        var countCheked = 0
        shoppingListModel.products.forEach {
            if (it.isChecked) countCheked++
        }
        binding.textViewCheckedProducts.text = countCheked.toString()
    }
}