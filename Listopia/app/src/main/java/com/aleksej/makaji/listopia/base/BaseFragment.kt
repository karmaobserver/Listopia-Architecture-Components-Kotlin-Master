package com.aleksej.makaji.listopia.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aleksej.makaji.listopia.di.Injectable
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
open class BaseFragment : Fragment(), Injectable {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    fun navigationController() = findNavController()

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resourceId: Int) {
        showToast(getString(resourceId))
    }

    fun showToastLong(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showToastLong(resourceId: Int) {
        showToast(getString(resourceId))
    }
}