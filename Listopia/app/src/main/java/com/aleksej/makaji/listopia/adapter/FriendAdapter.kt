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
                        && oldItem.friends == newItem.friends
            }
        }
) {

    private var mEditors: List<UserModel>? = null

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
                if (checkIfUserIsInEditors(it)) {
                    mFriendAdapterEvents.invoke(FriendAdapterEvents.FriendClick(it, false))
                } else {
                    mFriendAdapterEvents.invoke(FriendAdapterEvents.FriendClick(it, true))
                }
            }
        }
        binding.imageButtonOptions.setOnClickListener { view ->
            binding.userModel?.let {
                mFriendAdapterEvents.invoke(FriendAdapterEvents.OptionsClick(view, it))
            }
        }
        return binding
    }

    override fun bind(binding: ItemFriendBinding, item: UserModel) {
        binding.userModel = item
        if (checkIfUserIsInEditors(item)) {
            binding.constraintViewFriends.setBackgroundResource(R.color.colorPrimaryDark)
        } else {
            binding.constraintViewFriends.setBackgroundResource(R.color.grey_40)
        }
    }

    fun setEditors(editors: List<UserModel>) {
        mEditors = editors
        notifyDataSetChanged()
    }

    private fun checkIfUserIsInEditors(userModel: UserModel): Boolean {
        mEditors?.forEach { editor ->
            if ( editor.id == userModel.id) {
                return true
            }
        }
        return false
    }
}
