package com.beemer.unofficial.fromis_9.preference

import android.content.Context
import com.beemer.unofficial.fromis_9.application.MyApplication

object Preference {
    private const val PREF_NAME = "MyPrefs"
    private val sharedPreferences = MyApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var isAscending: Boolean
        get() = sharedPreferences.getBoolean("isAscending", true)
        set(value) = sharedPreferences.edit().putBoolean("isAscending", value).apply()

    var sortBy: String
        get() = sharedPreferences.getString("sortBy", "release") ?: "release"
        set(value) = sharedPreferences.edit().putString("sortBy", value).apply()
}