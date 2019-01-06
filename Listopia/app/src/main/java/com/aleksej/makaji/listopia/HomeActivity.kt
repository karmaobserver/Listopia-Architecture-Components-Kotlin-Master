package com.aleksej.makaji.listopia

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import com.aleksej.makaji.listopia.base.BaseActivity
import com.aleksej.makaji.listopia.databinding.ActivityHomeBinding
import com.aleksej.makaji.listopia.databinding.HeaderDrawerBinding

class HomeActivity : BaseActivity() {

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

    private fun setupNavigationDrawerMenu() {
        headerBinding = HeaderDrawerBinding.bind(binding.navigationView.getHeaderView(0))
        checkIfUserLoggedIn()
        binding.navigationView.itemIconTintList = null
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
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
                else -> {
                    showToast(R.string.navigation_something_els)
                    true
                }
            }
        }
    }

    private fun setupNavigationListener() {
        mNavigationListener = NavController.OnDestinationChangedListener { controller, destination, bundle ->
            when (destination.id) {
                R.id.fragment_shopping_list -> supportActionBar?.setTitle(R.string.title_shopping_list)
                else -> {}
            }
        }
        mNavController.addOnDestinationChangedListener(mNavigationListener)
    }

    private fun initClickListener() {
        headerBinding.buttonSignInUp.setOnClickListener {
        }
    }

    private fun checkIfUserLoggedIn() {
        if (true) {
            headerBinding.groupSignedIn.visibility = View.VISIBLE
            headerBinding.groupSignedOut.visibility = View.GONE
        } else {
            headerBinding.groupSignedIn.visibility = View.GONE
            headerBinding.groupSignedOut.visibility = View.VISIBLE
        }
    }
}
