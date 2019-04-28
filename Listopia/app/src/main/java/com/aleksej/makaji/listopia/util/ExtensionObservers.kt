package com.aleksej.makaji.listopia.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.aleksej.makaji.listopia.error.ListopiaError

/**
 * Created by Aleksej Makaji on 4/28/19.
 */

/**
 * Use if you want observe on just once (if you rotate phone the value will not be pushed to livedata)
 */
fun <T : Any, L : LiveData<StateHandler<T>>> BaseFragment.observeSingle(
        liveData: L,
        onSuccess: (data: T) -> Unit,
        onError: ((error: ListopiaError) -> Unit)? = null,
        onLoading: (() -> Unit)? = null,
        onHideLoading: (() -> Unit)? = null
) {
    observe(liveData, onSuccess, onError, onLoading, onHideLoading, true)
}

/**
 * Use if you want observe on every change (if you rotate phone the value will be pushed to livedata)
 */
fun <T : Any, L : LiveData<StateHandler<T>>> BaseFragment.observePeek(
        liveData: L,
        onSuccess: (data: T) -> Unit,
        onError: ((error: ListopiaError) -> Unit)? = null,
        onLoading: (() -> Unit)? = null,
        onHideLoading: (() -> Unit)? = null
) {
    observe(liveData, onSuccess, onError, onLoading, onHideLoading, false)
}

private fun <T : Any, L : LiveData<StateHandler<T>>> BaseFragment.observe(
        liveData: L,
        onSuccess: (data: T) -> Unit,
        onError: ((error: ListopiaError) -> Unit)? = null,
        onLoading: (() -> Unit)? = null,
        onHideLoading: (() -> Unit)? = null,
        isSingleEvent: Boolean
) {
    liveData.observe(this, Observer {
        when (it) {
            is StateHandler<T> -> {
                val eventHandler = if (isSingleEvent) it.getContentIfNotHandled() else it.peekContent()
                eventHandler?.let {
                    when (it.state) {
                        is State.Success -> {
                            onHideLoading?.invoke()
                            onHideLoading.isNull { hideLoading() }
                            it.state.data?.run{
                                onSuccess(this)
                            }
                        }

                        is State.Error -> {
                            onHideLoading?.invoke()
                            onHideLoading.isNull { hideLoading() }
                            onError?.run {
                                invoke(it.state.error)
                            }.isNull {
                                showError(it.state.error)
                            }
                        }
                        is State.Loading -> {
                            onLoading?.run {
                                invoke()
                            }.isNull {
                                showLoading()
                            }
                        }
                    }
                }
            }
        }
    })
}