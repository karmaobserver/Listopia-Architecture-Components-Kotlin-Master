package com.aleksej.makaji.listopia.util

import android.content.Context
import android.content.SharedPreferences
import com.aleksej.makaji.listopia.R

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class SharedPreferenceManager constructor(private val sharedPreferences: SharedPreferences, private val context: Context) {

    companion object {
        private const val TOKEN = "TOKEN"
        private const val USER_ID = "USER_ID"
        private const val LANGUAGE_CODE = "LANGUAGE_CODE"
    }

    private fun getSettingsPreferences(): SharedPreferences {
        return context.getSharedPreferences("com.aleksej.makaji.listopia.dev_preferences", Context.MODE_PRIVATE)
    }

    var token: String
        get() = sharedPreferences.getString(TOKEN, "").orEmpty()
        set(googleToken) = sharedPreferences.edit().putString(TOKEN, googleToken).apply()

    var userId: String
        get() = sharedPreferences.getString(USER_ID, "").orEmpty()
        set(userId) = sharedPreferences.edit().putString(USER_ID, userId).apply()

    var currency: String
        get() = getSettingsPreferences().getString(context.getString(R.string.key_currency), "$").orEmpty()
        set(currency) = getSettingsPreferences().edit().putString(context.getString(R.string.key_currency), currency).apply()

    var languageCode: String
        get() = sharedPreferences.getString(LANGUAGE_CODE, "en").orEmpty()
        set(languageCode) = sharedPreferences.edit().putString(LANGUAGE_CODE, languageCode).apply()

    fun clearAll(){
        sharedPreferences.edit().clear().apply()
    }
}