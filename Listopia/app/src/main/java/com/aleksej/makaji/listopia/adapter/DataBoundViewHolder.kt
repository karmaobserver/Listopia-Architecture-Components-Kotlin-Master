package com.aleksej.makaji.listopia.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Aleksej Makaji on 1/8/19.
 */

/**
 * A generic ViewHolder that works with a [ViewDataBinding].
 * @param <T> The type of the ViewDataBinding.
</T> */
class DataBoundViewHolder<out T : ViewDataBinding> constructor(val binding: T) :
        RecyclerView.ViewHolder(binding.root)