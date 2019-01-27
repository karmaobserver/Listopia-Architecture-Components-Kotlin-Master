package com.aleksej.makaji.listopia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.databinding.ItemProductBinding

/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class ProductAdapter(private val mDataBindingComponent: DataBindingComponent) : DataBoundPagedListAdapter<ProductModel, ItemProductBinding>(
        diffCallback = object : DiffUtil.ItemCallback<ProductModel>() {
            override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
                return oldItem.name == newItem.name
                        && oldItem.quantity == newItem.quantity
                        && oldItem.unit == newItem.unit
                        && oldItem.price == newItem.price
                        && oldItem.notes == newItem.notes
                        && oldItem.isChecked == newItem.isChecked
            }
        }
) {

    override fun createBinding(parent: ViewGroup): ItemProductBinding {
        val binding = DataBindingUtil.inflate<ItemProductBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_product,
                parent,
                false,
                mDataBindingComponent
        )
        return binding
    }

    override fun bind(binding: ItemProductBinding, item: ProductModel) {
        binding.productModel = item
    }
}