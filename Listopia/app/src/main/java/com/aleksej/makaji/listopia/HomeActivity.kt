package com.aleksej.makaji.listopia

import android.os.Bundle
import com.aleksej.makaji.listopia.base.BaseActivity
import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import javax.inject.Inject

class HomeActivity : BaseActivity() {

    @Inject
    lateinit var mSharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}
