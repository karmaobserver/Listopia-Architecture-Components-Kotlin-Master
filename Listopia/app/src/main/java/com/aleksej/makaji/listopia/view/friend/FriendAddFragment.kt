package com.aleksej.makaji.listopia.view.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.binding.FragmentDataBindingComponent
import com.aleksej.makaji.listopia.data.usecase.value.SaveFriendValue
import com.aleksej.makaji.listopia.databinding.FragmentFriendAddBinding
import com.aleksej.makaji.listopia.error.EmailError
import com.aleksej.makaji.listopia.error.UserDoesNotExistsError
import com.aleksej.makaji.listopia.util.*
import com.aleksej.makaji.listopia.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_friend_add.*
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-10-10.
 */
class FriendAddFragment: BaseFragment() {

    @Inject
    lateinit var mDialogController: DialogController

    private var binding by autoCleared<FragmentFriendAddBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentFriendAddBinding>(
                inflater,
                R.layout.fragment_friend_add,
                container,
                false,
                mDataBindingComponent
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mUserViewModel = viewModel(mViewModelFactory)
        initListeners()
        initObservers()
        showKeyboard()
    }

    private fun initObservers() {
        observeSaveFriend()
    }

    private fun observeSaveFriend() {
        observeSingle(mUserViewModel.saveFriendLiveData, {
            hideKeyboard()
            showToastLong(R.string.success_friend_add)
            findNavController().navigateUp()
        }, onError = {

            when (it) {
                is EmailError -> binding.textInputLayoutFriendEmail.error = getString(it.resourceId)
                is UserDoesNotExistsError  -> {
                    mDialogController.showDialog(
                            getString(R.string.dialog_title_friend_invitation),
                            getString(R.string.dialog_message_friend_invitation),
                            getString(R.string.action_send),
                            getString(R.string.action_cancel),
                            { sendFriendInvitation() },
                            {})
                }
                else -> showError(it)
            }
        }, onLoading = {
            showLoading()
            button_add_friend.isEnabled = false
        }, onHideLoading = {
            button_add_friend.isEnabled = true
            hideLoading()
        })
    }

    private fun initListeners() {
        button_add_friend.setOnClickListener {
            addFriend()
        }
    }

    private fun addFriend() {
        mUserViewModel.saveFriend(SaveFriendValue(binding.editTextFriendEmail.text()))
    }

    private fun sendFriendInvitation() {
        //TODO: Notify backend to send email invitation to friend
        findNavController().navigateUp()
    }
}