package com.aleksej.makaji.listopia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.databinding.ItemFriendBinding

/**
 * Created by Aleksej Makaji on 2019-10-10.
 */
class FriendAdapter(private val mDataBindingComponent: DataBindingComponent, private val mFriendAdapterEvents: (FriendAdapterEvents) -> Unit) : DataBoundListAdapter<UserModel, ItemFriendBinding>(
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

    override fun createBinding(parent: ViewGroup): ItemFriendBinding {
        val binding = DataBindingUtil.inflate<ItemFriendBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_friend,
                parent,
                false,
                mDataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.userModel?.let {
                mFriendAdapterEvents.invoke(FriendAdapterEvents.FriendClick(it))
            }
        }
        binding.imageButtonShare.setOnClickListener { view ->
            binding.userModel?.let {
                mFriendAdapterEvents.invoke(FriendAdapterEvents.ShareClick(it))
            }
        }
        return binding
    }

    override fun bind(binding: ItemFriendBinding, item: UserModel) {
        binding.userModel = item
    }
}
