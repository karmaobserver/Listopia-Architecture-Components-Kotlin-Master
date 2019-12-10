package com.aleksej.makaji.listopia.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.data.repository.model.ProductModel
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.databinding.ItemShoppingListBinding
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.aleksej.makaji.listopia.util.putVisibleOrGone
import java.util.*



/**
 * Created by Aleksej Makaji on 1/8/19.
 */
class ShoppingListAdapter(private val mDataBindingComponent: DataBindingComponent, private val mSharedPreferenceManager: SharedPreferenceManager, private val mShoppingListAdapterEvents: (ShoppingListAdapterEvents) -> Unit) : DataBoundPagedListAdapter<ShoppingListModel, ItemShoppingListBinding>(
        diffCallback = object : DiffUtil.ItemCallback<ShoppingListModel>() {
            override fun areItemsTheSame(oldItem: ShoppingListModel, newItem: ShoppingListModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ShoppingListModel, newItem: ShoppingListModel): Boolean {
                return oldItem.name == newItem.name
                        && oldItem.ownerId == newItem.ownerId
                        && oldItem.isSynced == newItem.isSynced
                        && oldItem.isDeleted == newItem.isDeleted
                        && oldItem.timestamp == newItem.timestamp
                        && oldItem.editors == newItem.editors
                        && oldItem.products == newItem.products
            }
        }
) {
    private val MAX_CLICK_DURATION = 200
    private var startClickTime = 0L

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
                mShoppingListAdapterEvents.invoke(ShoppingListAdapterEvents.OptionsClick(view, it))
            }
        }

        binding.recyclerViewEditors.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                 when (e.action) {
                     MotionEvent.ACTION_DOWN -> {
                         startClickTime = Calendar.getInstance().timeInMillis
                         return false
                     }
                     MotionEvent.ACTION_UP -> {
                         val clickDuration = Calendar.getInstance().timeInMillis - startClickTime
                         if (clickDuration < MAX_CLICK_DURATION) {
                             //click event has occurred
                             binding.shoppingListModel?.let {
                                 mShoppingListAdapterEvents.invoke(ShoppingListAdapterEvents.ShoppingListClick(it.id, it.name))
                                 return true
                             }
                         }
                     }
                 }
                 return false
            }
        })
        return binding
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun bind(binding: ItemShoppingListBinding, item: ShoppingListModel) {
        binding.shoppingListModel = item
        countCheckedProducts(binding, item)
        setEditorAdapter(binding, item.editors)
    }

    private fun countCheckedProducts(binding: ItemShoppingListBinding, shoppingListModel: ShoppingListModel) {
        val productsNotDeleted = arrayListOf<ProductModel>()
        shoppingListModel.products?.forEach {
            if (!it.isDeleted) {
                productsNotDeleted.add(it)
            }
        }
        if (productsNotDeleted.isNullOrEmpty()) {
            binding.textViewMaxProducts.text = "0"
            binding.textViewCheckedProducts.text = "0"
            binding.progressBarShoppingList.max = 0
            return
        } else {
            val maxProducts = productsNotDeleted.size
            binding.textViewMaxProducts.text = maxProducts.toString()
            binding.progressBarShoppingList.max = maxProducts
        }
        var countCheked = 0
        productsNotDeleted.forEach {
            if (it.isChecked) countCheked++
        }
        binding.textViewCheckedProducts.text = countCheked.toString()
        binding.progressBarShoppingList.progress = countCheked
    }

    private fun setEditorAdapter(binding: ItemShoppingListBinding, editors: List<UserModel>?) {
        if (editors.isNullOrEmpty()) {
            binding.recyclerViewEditors.putVisibleOrGone(false)
        } else {
            binding.recyclerViewEditors.putVisibleOrGone(true)
            val editorAdapter = EditorAdapter(mDataBindingComponent)
            binding.recyclerViewEditors.adapter = editorAdapter

            val editorsWithoutOwner = editors.toMutableList()
            editors.forEach {
                if (it.id == mSharedPreferenceManager.userId) {
                    editorsWithoutOwner.remove(it)
                    return@forEach
                }
            }

            editorAdapter.submitList(editorsWithoutOwner)
        }
    }
}