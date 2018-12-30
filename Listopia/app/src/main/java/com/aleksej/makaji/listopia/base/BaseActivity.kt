package com.aleksej.makaji.listopia.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
open class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector, LifecycleOwner {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resourceId: Int) {
        showToast(getString(resourceId))
    }


}