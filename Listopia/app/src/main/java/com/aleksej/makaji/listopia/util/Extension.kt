package com.aleksej.makaji.listopia.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.aleksej.makaji.listopia.data.event.Event
import com.aleksej.makaji.listopia.data.event.EventHandler

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}

fun <T : Any, L : LiveData<EventHandler<T>>> LifecycleOwner.observePeekFromActivity(liveData: L, body: (Event<out T?>) -> Unit) =
        liveData.observe(this, Observer {
            it?.peekContent()?.let {
                body(it.event)
            }
        })

fun <T : Any, L : LiveData<EventHandler<T>>> LifecycleOwner.observeSingleFromActivity(liveData: L, body: (Event<out T?>) -> Unit) =
        liveData.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {
                body(it.event)
            }
        })

fun <T : Any, L : LiveData<EventHandler<T>>> Fragment.observePeek(liveData: L, body: (Event<out T?>) -> Unit) =
        liveData.observe(viewLifecycleOwner,  Observer {
            it?.peekContent()?.let {
                body(it.event)
            }
        })

fun <T : Any, L : LiveData<EventHandler<T>>> Fragment.observeSingle(liveData: L, body: (Event<out T?>) -> Unit) =
        liveData.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                body(it.event)
            }
        })