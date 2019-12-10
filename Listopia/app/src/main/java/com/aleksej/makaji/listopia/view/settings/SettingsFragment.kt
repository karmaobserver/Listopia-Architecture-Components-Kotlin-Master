package com.aleksej.makaji.listopia.view.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.*
import com.aleksej.makaji.listopia.R

/**
 * Created by Aleksej Makaji on 2019-12-10.
 */
class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        // show the current value in the settings screen (Summery INIT)
        for (i in 0 until preferenceScreen.preferenceCount) {
            pickPreferenceObject(preferenceScreen.getPreference(i))
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = key?.let { findPreference<Preference>(it) }

        if (preference != null) {

            if (preference !is CheckBoxPreference) {
                val value = sharedPreferences?.getString(preference.key, "")
                value?.let {
                    setPreferenceSummery(preference, it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    /**
     * Check if it is instance of PreferenceCateogry, in case it is, check childs of that preference and do recursion.
     * In case it is not instance of PreferenceCateogry, get value of shared preference and call method to set preference summery depended on preference.
     * @param preference
     */
    private fun pickPreferenceObject(preference: Preference) {
        if (preference is PreferenceCategory) {
            for (i in 0 until preference.preferenceCount) {
                pickPreferenceObject(preference.getPreference(i))
            }
        } else {
            //CheckBoxPreference is auto handled, so we do not need to implement logic for it
            if (preference !is CheckBoxPreference) {
                val value = preferenceScreen.sharedPreferences.getString(preference.key, "")
                setPreferenceSummery(preference, value)
            }
        }
    }

    private fun setPreferenceSummery(preference: Preference, value: Any) {

        val stringValue = value.toString()

        if (preference is ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            val prefIndex = preference.findIndexOfValue(stringValue)
            //same code in one line
            //int prefIndex = ((ListPreference) preference).findIndexOfValue(value);

            //prefIndex must be is equal or garter than zero because
            //array count as 0 to ....
            if (prefIndex >= 0) {
                preference.setSummary(preference.entries[prefIndex])
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.summary = stringValue
        }
    }


}