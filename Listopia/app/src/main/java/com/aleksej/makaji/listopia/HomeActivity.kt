package com.aleksej.makaji.listopia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aleksej.makaji.listopia.base.BaseActivity
import com.aleksej.makaji.listopia.data.repository.model.UserModel
import com.aleksej.makaji.listopia.databinding.ActivityHomeBinding
import com.aleksej.makaji.listopia.databinding.HeaderDrawerBinding
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import com.aleksej.makaji.listopia.util.hideKeyboard
import com.aleksej.makaji.listopia.util.putVisibleOrGone
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import javax.inject.Inject

class HomeActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CODE_SIGN_IN = 100
    }

    @Inject
    lateinit var mSharedPreferenceManager: SharedPreferenceManager

    private lateinit var binding: ActivityHomeBinding

    private lateinit var headerBinding: HeaderDrawerBinding

    private lateinit var mNavController: NavController

    private lateinit var mAppBarConfiguration: AppBarConfiguration

    private lateinit var mNavigationListener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)

        mNavController = Navigation.findNavController(this, R.id.fragment_navigation_host)
        mAppBarConfiguration = AppBarConfiguration(mNavController.graph, binding.drawerLayout)

        // Set up ActionBar
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(mNavController, mAppBarConfiguration)

        // Set up navigation menu for drawer layout
        binding.navigationView.setupWithNavController(mNavController)

        setupNavigationDrawerMenu()
        setupNavigationListener()
        initClickListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        hideKeyboard()
        return mNavController.navigateUp(mAppBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mNavController.removeOnDestinationChangedListener(mNavigationListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    handleViewForLoggedUser(it)
                }
            } else {
                showToast(response?.error?.message.toString())
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
            checkIfUserLoggedIn()
        }
    }

    private fun setupNavigationDrawerMenu() {
        headerBinding = HeaderDrawerBinding.bind(binding.navigationView.getHeaderView(0))
        checkIfUserLoggedIn()
        binding.navigationView.itemIconTintList = null
        /*val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {}
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()*/
        binding.navigationView.setNavigationItemSelectedListener {
            //If we want to highlight selected item
            //it.isCheckable = true
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.navigation_trash -> {
                    showToast(R.string.navigation_trash)
                    true
                }
                R.id.navigation_feedback -> {
                    showToast(R.string.navigation_feedback)
                    true
                }
                R.id.navigation_recommend -> {
                    showToast(R.string.navigation_recommend)
                    true
                }
                R.id.navigation_help -> {
                    showToast(R.string.navigation_help)
                    true
                }
                R.id.navigation_settings -> {
                    showToast(R.string.navigation_settings)
                    true
                }
                R.id.navigation_sign_out -> {
                    signOut()
                    true
                }
                else -> {
                    showToast(R.string.navigation_something_els)
                    true
                }
            }
        }
    }

    private fun setupNavigationListener() {
        mNavigationListener = NavController.OnDestinationChangedListener { controller, destination, _bundle ->
            when (destination.id) {
                R.id.fragment_shopping_list -> supportActionBar?.setTitle(R.string.title_shopping_list)
                else -> {}
            }
        }
        mNavController.addOnDestinationChangedListener(mNavigationListener)
    }

    private fun initClickListener() {
        headerBinding.buttonSignInUp.setOnClickListener {
            // Choose authentication providers
            val providers = Arrays.asList(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build())

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.ic_favorites)
                            .build(),
                    REQUEST_CODE_SIGN_IN)
        }
    }

    private fun checkIfUserLoggedIn() {
        if (mSharedPreferenceManager.userUid != "") {
            headerBinding.groupSignedIn.putVisibleOrGone(true)
            headerBinding.groupSignedOut.putVisibleOrGone(false)
        } else {
            headerBinding.groupSignedIn.putVisibleOrGone(false)
            headerBinding.groupSignedOut.putVisibleOrGone(true)
        }
    }

    private fun handleViewForLoggedUser(user: FirebaseUser) {
        mSharedPreferenceManager.userUid = user.uid
        user.getIdToken(true).addOnSuccessListener {
            it.token?.let {
                mSharedPreferenceManager.token = it
            }
        }
        val userModel = UserModel(user.uid, user.displayName, user.email, user.photoUrl)
        headerBinding.userModel = userModel
    }

    private fun signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    mSharedPreferenceManager.userUid = ""
                    mSharedPreferenceManager.token = ""
                    checkIfUserLoggedIn()
                    showToast("Successfully Signed Out")
                }
    }

    fun showProgress(){
        progress_bar.visibility = View.VISIBLE
    }

    fun hideProgress(){
        progress_bar.visibility = View.INVISIBLE
    }
}
