package com.basekotlin.app.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.support.compat.BuildConfig


open class PreferencesHelper(context: Context) {

    private val settings: SharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    protected fun getInt(key: String, defaultValue: Int): Int {
        return settings.getInt(key, defaultValue)
    }

    protected fun setInt(key: String, value: Int) {
        val editor = settings.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    protected fun getLong(key: String, defaultValue: Long): Long {
        return settings.getLong(key, defaultValue)
    }

    protected fun setLong(key: String, value: Long) {
        val editor = settings.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    protected fun getFloat(key: String, defaultValue: Float): Float {
        return settings.getFloat(key, defaultValue)
    }

    protected fun setFloat(key: String, value: Float) {
        val editor = settings.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    protected fun getString(key: String, defaultValue: String): String? {
        return settings.getString(key, defaultValue)
    }

    protected fun setString(key: String, value: String) {
        val editor = settings.edit()
        editor.putString(key, value)
        editor.apply()
    }

    protected fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return settings.getBoolean(key, defaultValue)
    }

    protected fun setBoolean(key: String, value: Boolean) {
        val editor = settings.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    protected fun clearAll() {
        val editor = settings.edit()
        editor.clear()
        editor.apply()
    }
}
