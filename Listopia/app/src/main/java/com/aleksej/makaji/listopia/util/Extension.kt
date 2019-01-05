package com.aleksej.makaji.listopia.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}

fun <T : Any, L : LiveData<StateHandler<T>>> LifecycleOwner.observePeekFromActivity(liveData: L, body: (State<out T?>) -> Unit) =
        liveData.observe(this, Observer {
            it?.peekContent()?.let {
                body(it.state)
            }
        })

fun <T : Any, L : LiveData<StateHandler<T>>> LifecycleOwner.observeSingleFromActivity(liveData: L, body: (State<out T?>) -> Unit) =
        liveData.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {
                body(it.state)
            }
        })

fun <T : Any, L : LiveData<StateHandler<T>>> Fragment.observePeek(liveData: L, body: (State<out T?>) -> Unit) =
        liveData.observe(viewLifecycleOwner,  Observer {
            it?.peekContent()?.let {
                body(it.state)
            }
        })

fun <T : Any, L : LiveData<StateHandler<T>>> Fragment.observeSingle(liveData: L, body: (State<out T?>) -> Unit) =
        liveData.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                body(it.state)
            }
        })