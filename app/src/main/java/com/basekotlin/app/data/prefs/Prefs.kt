package com.basekotlin.app.data.prefs


interface Prefs {

    fun getToken(): String?
    fun setToken(token: String)
    fun getId(): String?
    fun setId(userId: String)


    fun clear()
}
