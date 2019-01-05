package com.aleksej.makaji.listopia.binding

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment

/**
 * Created by Aleksej Makaji on 1/2/19.
 */

/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent(fragment: Fragment) : DataBindingComponent {
    private val adapter = FragmentBindingAdapters(fragment)

    override fun getFragmentBindingAdapters() = adapter
}