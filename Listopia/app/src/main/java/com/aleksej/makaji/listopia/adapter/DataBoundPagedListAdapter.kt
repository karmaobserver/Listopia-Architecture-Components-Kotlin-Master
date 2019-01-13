package com.aleksej.makaji.listopia.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Aleksej Makaji on 1/8/19.
 */

/**
 * A generic RecyclerView adapter that uses Data Binding & DiffUtil.
 *
 * @param <T> Type of the items in the list
 * @param <V> The type of the ViewDataBinding
</V></T> */
abstract class DataBoundPagedListAdapter<T, V : ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, DataBoundViewHolder<V>>(
        AsyncDifferConfig.Builder<T>(diffCallback)
                .setBackgroundThreadExecutor { runnable -> Thread(runnable).start() }
                .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        getItem(position)?.let {
            bind(holder.binding, it)
        }
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(binding: V, item: T)
}