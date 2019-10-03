package com.aleksej.makaji.listopia.util

import android.content.SharedPreferences

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class SharedPreferenceManager constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val TOKEN = "TOKEN"
        private const val USER_ID = "USER_ID"
        private const val CURRENCY = "CURRENCY"
        private const val LANGUAGE_CODE = "LANGUAGE_CODE"
    }

    var token: String
        get() = sharedPreferences.getString(TOKEN, "").orEmpty()
        set(googleToken) = sharedPreferences.edit().putString(TOKEN, googleToken).apply()

    var userId: String
        get() = sharedPreferences.getString(USER_ID, "").orEmpty()
        set(userId) = sharedPreferences.edit().putString(USER_ID, userId).apply()

    var currency: String
        get() = sharedPreferences.getString(CURRENCY, "$").orEmpty()
        set(currency) = sharedPreferences.edit().putString(CURRENCY, currency).apply()

    var languageCode: String
        get() = sharedPreferences.getString(LANGUAGE_CODE, "en").orEmpty()
        set(languageCode) = sharedPreferences.edit().putString(LANGUAGE_CODE, languageCode).apply()
}