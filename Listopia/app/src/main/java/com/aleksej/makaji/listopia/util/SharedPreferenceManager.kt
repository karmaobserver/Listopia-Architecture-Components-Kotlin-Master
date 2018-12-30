package com.aleksej.makaji.listopia.util

import android.content.SharedPreferences

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class SharedPreferenceManager constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val TOKEN = "TOKEN"
    }

    var token: String
        get() = sharedPreferences.getString(TOKEN, "").orEmpty()
        set(googleToken) = sharedPreferences.edit().putString(TOKEN, googleToken).apply()
}