package com.aleksej.makaji.listopia.util

import android.content.SharedPreferences

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class SharedPreferenceManager constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val TOKEN = "TOKEN"
        private const val USER_UID = "USER_UID"
        private const val CURRENCY = "CURRENCY"
    }

    var token: String
        get() = sharedPreferences.getString(TOKEN, "").orEmpty()
        set(googleToken) = sharedPreferences.edit().putString(TOKEN, googleToken).apply()

    var userUid: String
        get() = sharedPreferences.getString(USER_UID, "").orEmpty()
        set(userUid) = sharedPreferences.edit().putString(USER_UID, userUid).apply()

    var currency: String
        get() = sharedPreferences.getString(CURRENCY, "$").orEmpty()
        set(currency) = sharedPreferences.edit().putString(CURRENCY, currency).apply()
}