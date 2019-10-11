package com.aleksej.makaji.listopia.view.friend

/**
 * Created by Aleksej Makaji on 2019-10-09.
 */

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.adapter.FriendAdapter
import com.aleksej.makaji.listopia.adapter.FriendAdapterEvents
import com.aleksej.makaji.listopia.base.BaseFragment
import com.aleksej.makaji.listopia.binding.FragmentDataBindingComponent
import com.aleksej.makaji.listopia.databinding.FragmentFriendListBinding
import com.aleksej.makaji.listopia.util.*
import com.aleksej.makaji.listopia.viewmodel.UserViewModel
import javax.inject.Inject

class FriendListFragment: BaseFragment() {

    @Inject
    lateinit var mSharedPreferenceManager: SharedPreferenceManager

    private lateinit var mUserViewModel: UserViewModel

    private var binding by autoCleared<FragmentFriendListBinding>()
    private var mDataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var mFriendAdapter by autoCleared<FriendAdapter>()

    private var mFriendId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentFriendListBinding>(
                inflater,
                R.layout.fragment_friend_list,
                container,
                false,
                mDataBindingComponent
        )
        setHasOptionsMenu(true)
        binding = dataBinding
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mUserViewModel = viewModel(mViewModelFactory)
        binding.userViewModel= mUserViewModel
        initData()
        initRecyclerView()
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_friend_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.submenu_options_delete_all -> {
                //TODO Delete all friends
                showToastLong("All friends has been deleted")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initData() {
        mUserViewModel.getUserById(mSharedPreferenceManager.userId)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.recyclerViewFriends.layoutManager = layoutManager
        binding.recyclerViewFriends.setHasFixedSize(false)

        mFriendAdapter = FriendAdapter(mDataBindingComponent) {
            when (it) {
                is FriendAdapterEvents.FriendClick -> {
                    //TODO
                }
                is FriendAdapterEvents.ShareClick -> {
                    //TODO
                }
            }
        }
        binding.recyclerViewFriends.adapter = mFriendAdapter
    }

    private fun initObservers() {
        observeFriends()
        observeAddProduct()
        observeDeleteProductById()
    }

    private fun observeDeleteProductById() {
     /*   observeSingle(mFriendViewModel.deleteProductByIdLiveData, {
            showToastLong(R.string.success_friend_delete)
        }, onError = {
            showError(it)
        })*/
    }

    private fun observeAddProduct() {
        observeSingle(mUserViewModel.addFriendEvent, {
            findNavController().navigate(FriendListFragmentDirections.actionFriendListFragmentToFriendAddFragment())
        })
    }

    private fun observeFriends() {
        observePeek(mUserViewModel.getUserLiveData, {
            mFriendAdapter.submitList(it.friends)
        }, onError = {
            showError(it)
        })
    }

}
