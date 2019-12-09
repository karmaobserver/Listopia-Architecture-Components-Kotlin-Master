package com.aleksej.makaji.listopia.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.databinding.ItemProductBinding
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.aleksej.makaji.listopia.util.margin
import com.aleksej.makaji.listopia.util.toDecimalString
import javax.inject.Inject
import androidx.constraintlayout.widget.ConstraintSet



/**
 * Created by Aleksej Makaji on 1/20/19.
 */
class ProductAdapter(private val mDataBindingComponent: DataBindingComponent,
                     private val mSharedPreferenceManager: SharedPreferenceManager,
                     private val mProductAdapterEvents: (ProductAdapterEvents) -> Unit) : DataBoundPagedListAdapter<ProductModel, ItemProductBinding>(
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
        binding.root.setOnClickListener {
            binding.productModel?.let {
                if (it.isChecked) {
                    it.isChecked = false
                    markItemAsUnchecked(binding)
                } else {
                    it.isChecked = true
                    markItemAsChecked(binding)
                }
                mProductAdapterEvents.invoke(ProductAdapterEvents.ProductClick(it))
            }
        }
        binding.imageButtonProductOptions.setOnClickListener { view ->
            binding.productModel?.let {
                mProductAdapterEvents.invoke(ProductAdapterEvents.OptionsClick(view, it))
            }
        }
        return binding
    }

    override fun bind(binding: ItemProductBinding, item: ProductModel) {
        binding.currency = mSharedPreferenceManager.currency
        binding.productModel = item
        //shouldSetBottomMargins(binding, item)
        if (item.quantity == 0.0 || item.quantity == null) {
            binding.textViewProductPrice.text = item.price.toDecimalString()
        } else {
            binding.textViewProductPrice.text = (item.price * item.quantity).toDecimalString()
        }
    }

    private fun markItemAsChecked(binding: ItemProductBinding) {
        binding.textViewProductName.paintFlags = binding.textViewProductName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.textViewProductNotes.paintFlags = binding.textViewProductNotes.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.textViewProductQuantity.paintFlags = binding.textViewProductQuantity.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.textViewProductUnit.paintFlags = binding.textViewProductUnit.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.textViewProductPrice.paintFlags = binding.textViewProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.textViewCurrency.paintFlags = binding.textViewCurrency.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.textViewProductName.isEnabled = false
        binding.textViewProductNotes.isEnabled = false
        binding.textViewProductQuantity.isEnabled = false
        binding.textViewProductUnit.isEnabled = false
        //Change it
        binding.textViewProductPrice.isEnabled = false
        binding.textViewCurrency.isEnabled = false
    }

    private fun markItemAsUnchecked(binding: ItemProductBinding) {
        binding.textViewProductName.paintFlags = binding.textViewProductName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        binding.textViewProductNotes.paintFlags = binding.textViewProductNotes.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        binding.textViewProductQuantity.paintFlags = binding.textViewProductQuantity.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        binding.textViewProductUnit.paintFlags = binding.textViewProductUnit.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        binding.textViewProductPrice.paintFlags = binding.textViewProductPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        binding.textViewCurrency.paintFlags = binding.textViewCurrency.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        binding.textViewProductName.isEnabled = true
        binding.textViewProductNotes.isEnabled = true
        binding.textViewProductQuantity.isEnabled = true
        binding.textViewProductUnit.isEnabled = true
        //Change it
        binding.textViewProductPrice.isEnabled = true
        binding.textViewCurrency.isEnabled = true
    }

    private fun shouldSetBottomMargins(binding: ItemProductBinding, productModel: ProductModel) {
        if (productModel.notes.trim() == "" && productModel.quantity == 0.0 && productModel.unit.trim() == "" && productModel.price == 0.0) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.constraintLayoutProduct)
            constraintSet.connect(binding.textViewProductName.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(binding.constraintLayoutProduct)
            binding.textViewProductName.margin(8f, 8f, 8f, 8f)
        } else if (productModel.quantity == 0.0 && productModel.unit.trim() == "" && productModel.price == 0.0) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.constraintLayoutProduct)
            constraintSet.connect(binding.textViewProductNotes.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(binding.constraintLayoutProduct)
            binding.textViewProductNotes.margin(8f, 0f, 8f, 8f)
        } else if (productModel.notes.trim() == "" && productModel.quantity == 0.0 && productModel.unit.trim() == "") {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.constraintLayoutProduct)
            constraintSet.connect(binding.textViewProductPrice.id, ConstraintSet.BOTTOM, binding.textViewProductName.id, ConstraintSet.BOTTOM, 0)
            constraintSet.connect(binding.textViewProductPrice.id, ConstraintSet.TOP, binding.textViewProductName.id, ConstraintSet.TOP, 0)
            constraintSet.applyTo(binding.constraintLayoutProduct)
            binding.textViewProductName.margin(8f, 8f, 8f, 8f)
        }
    }
}