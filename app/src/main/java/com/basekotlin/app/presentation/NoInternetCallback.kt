package com.basekotlin.app.presentation


interface NoInternetCallback {
    fun onNetworkConnectionChanged(isNetworkConnected: Boolean)
}
