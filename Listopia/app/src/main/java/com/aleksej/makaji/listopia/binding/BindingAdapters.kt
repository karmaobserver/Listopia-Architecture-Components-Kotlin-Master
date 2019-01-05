package com.aleksej.makaji.listopia.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Created by Aleksej Makaji on 12/31/18.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    /*@JvmStatic
    @BindingAdapter("imageUrl")
    fun ImageView.setImageUrl(url: String?) {
        Glide.with(context).load(url).into(this)
    }*/
}