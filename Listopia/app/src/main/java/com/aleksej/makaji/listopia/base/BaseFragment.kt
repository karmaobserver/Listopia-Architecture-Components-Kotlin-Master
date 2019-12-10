package com.aleksej.makaji.listopia.base

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aleksej.makaji.listopia.HomeActivity
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.di.Injectable
import com.aleksej.makaji.listopia.error.*
import com.aleksej.makaji.listopia.util.observeSingle
import com.aleksej.makaji.listopia.util.viewModel
import com.aleksej.makaji.listopia.viewmodel.UserViewModel
import javax.inject.Inject



/**
 * Created by Aleksej Makaji on 12/30/18.
 */
open class BaseFragment : Fragment(), Injectable {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var mUserViewModel: UserViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mUserViewModel = viewModel(mViewModelFactory)
        observeRemoveSession()
    }

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

    open fun showError(error: ListopiaError) {
        when(error){
            is BackendError -> {
                showToastLong(error.response.message)
            }
            is ExceptionError -> {
                showToast(error.exception.localizedMessage)
            }
            is UnauthorizedError ->{
                mUserViewModel.removeSession()
            }
            is RoomError -> {
                showToast(R.string.error_room)
            }
            is ThrowableError -> {
                showToast(R.string.error_unknown)
            }
        }
    }

    private fun observeRemoveSession() {
        observeSingle(mUserViewModel.removeSessioEventLiveData, {
            (activity as HomeActivity).signOutUiLogic()
        }, onError = {
            showError(it)
        })
    }

    open fun showLoading() {
        (activity as? HomeActivity)?.showProgress()
    }

    open fun hideLoading() {
        (activity as? HomeActivity)?.hideProgress()
    }
}