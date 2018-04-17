package com.basekotlin.app.data.prefs

import android.content.Context


class PreferencesManager(context: Context) : PreferencesHelper(context), Prefs {

    override fun clear() {
        setId("")
        setToken("")
    }

    override fun getToken(): String? {
        return getString(PROP_TOKEN, "")
    }

    override fun setToken(token: String) {
        setString(PROP_TOKEN, token)
    }

    override fun getId(): String? {
        return getString(PROP_ID, "")
    }

    override fun setId(userId: String) {
        setString(PROP_ID, userId)
    }

    companion object {

        private val PROP_TOKEN = "prop_token"
        private val PROP_ID = "prop_id"
    }

}
