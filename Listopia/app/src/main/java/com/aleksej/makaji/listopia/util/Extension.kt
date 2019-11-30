package com.aleksej.makaji.listopia.util

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.StateHandler
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}

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

/**
 * Extension for setting margin programmatically to any view which has Margin params. Values are scallable.
 */
fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

fun Any?.isNull(onNull: () -> Unit) {
    if (this == null) {
        onNull.invoke()
    }
}

/**
 * Use to map object ot another object
 */

inline fun <reified T : Any> Any.mapTo(): T =
        GsonBuilder().create().run {
            toJson(this@mapTo).let { fromJson(it, T::class.java) }
        }

fun Double.toDecimalString(): String {
    val decimalFormat = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
    decimalFormat.maximumFractionDigits = 340
    return decimalFormat.format(this)
}