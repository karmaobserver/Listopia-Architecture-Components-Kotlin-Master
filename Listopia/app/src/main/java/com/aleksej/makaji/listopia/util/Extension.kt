package com.aleksej.makaji.listopia.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.google.android.material.textfield.TextInputEditText

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

fun View.putVisibleOrInvisible(value: Boolean) {
    this.visibility = if (value) View.VISIBLE else View.INVISIBLE
}

fun View.putVisibleOrGone(value: Boolean) {
    this.visibility = if (value) View.VISIBLE else View.GONE
}

fun EditText.text(): String {
    return text.toString()
}

fun EditText.textDouble(): Double {
    return try {
        text.toString().toDouble()
    } catch (e: Exception) {
        0.0
    }
}

fun Fragment.hideKeyboard() {
    this.view?.let {
        activity?.hideKeyboard(it)
    }
}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showKeyboard() {
    activity?.showKeyboard()
}

fun Context.showKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun TextInputEditText.onSubmit(func: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->

        if (actionId == EditorInfo.IME_ACTION_DONE) {
            func()
        }
        true
    }
}