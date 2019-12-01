package com.aleksej.makaji.listopia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.databinding.ItemEditorBinding

/**
 * Created by Aleksej Makaji on 2019-12-01.
 */
class EditorAdapter(private val mDataBindingComponent: DataBindingComponent) : DataBoundListAdapter<UserModel, ItemEditorBinding>(
        diffCallback = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem.name == newItem.name
                        && oldItem.avatar == newItem.avatar
            }
        }
) {

    override fun createBinding(parent: ViewGroup): ItemEditorBinding {
        val binding = DataBindingUtil.inflate<ItemEditorBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_editor,
                parent,
                false,
                mDataBindingComponent
        )
        return binding
    }

    override fun bind(binding: ItemEditorBinding, item: UserModel) {
        binding.userModel = item
        if (item.avatar == null) {
            binding.imageViewAvatar.setImageResource(R.drawable.background_header)
        }
    }
}
