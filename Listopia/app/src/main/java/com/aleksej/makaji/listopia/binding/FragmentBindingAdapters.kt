package com.aleksej.makaji.listopia.binding

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 1/2/19.
 */

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: Uri?) {
        Glide.with(fragment).load(url).into(imageView)
    }
}