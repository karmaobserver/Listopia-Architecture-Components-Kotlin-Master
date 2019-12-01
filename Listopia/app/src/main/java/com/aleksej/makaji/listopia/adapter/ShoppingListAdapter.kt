package com.aleksej.makaji.listopia.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.data.repository.model.ShoppingListModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.databinding.ItemShoppingListBinding
import com.aleksej.makaji.listopia.util.putVisibleOrGone
import java.util.*



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
                mShoppingListAdapterEvents.invoke(ShoppingListAdapterEvents.OptionsClick(view, it.id))
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

    override fun bind(binding: ItemShoppingListBinding, item: ShoppingListModel) {
        binding.shoppingListModel = item
        countCheckedProducts(binding, item)
        setEditorAdapter(binding, item.editors)
    }

    private fun countCheckedProducts(binding: ItemShoppingListBinding, shoppingListModel: ShoppingListModel) {
        if (shoppingListModel.products.isNullOrEmpty()) {
            binding.textViewMaxProducts.text = "0"
            binding.textViewCheckedProducts.text = "0"
            binding.progressBarShoppingList.max = 0
            return
        } else {
            val maxProducts = shoppingListModel.products.size
            binding.textViewMaxProducts.text = maxProducts.toString()
            binding.progressBarShoppingList.max = maxProducts
        }
        var countCheked = 0
        shoppingListModel.products.forEach {
            if (it.isChecked) countCheked++
        }
        binding.textViewCheckedProducts.text = countCheked.toString()
        binding.progressBarShoppingList.progress = countCheked
    }

    private fun setEditorAdapter(binding: ItemShoppingListBinding, friends: List<UserModel>?) {
        if (friends.isNullOrEmpty()) {
            binding.recyclerViewEditors.putVisibleOrGone(false)
        } else {
            binding.recyclerViewEditors.putVisibleOrGone(true)
            val editorAdapter = EditorAdapter(mDataBindingComponent)
            binding.recyclerViewEditors.adapter = editorAdapter
            editorAdapter.submitList(friends)
        }
    }
}