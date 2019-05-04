package com.aleksej.makaji.listopia.binding

import android.graphics.Paint
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.bumptech.glide.Glide
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import java.text.DecimalFormat


/**
 * Created by Aleksej Makaji on 12/31/18.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(imageView).load(url).into(imageView)
    }

    //For Double value in text view
    @JvmStatic
    @BindingAdapter("android:text")
    fun setDouble(view: TextView, value: Double) {
        when {
            value.isNaN() -> view.text = ""
            value == 0.0 -> view.text = ""
            else -> {
                val decimalFormater = DecimalFormat("###############.##")
                view.text = decimalFormater.format(value)
            }
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "android:text")
    fun getDouble(view: TextView): Double {
        val number = view.text.toString()
        if (number.isEmpty()) return 0.0
        return try {
            number.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    @JvmStatic
    @BindingAdapter("strikeThrough")
    fun setStrikeThrough(view: TextView, strikeThrough: Boolean) {
        if (strikeThrough) {
            view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            view.isEnabled = false
        } else {
            view.paintFlags = view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            view.isEnabled = true
        }

    }
}