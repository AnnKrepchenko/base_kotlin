package com.basekotlin.app.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.text.TextUtils

/**
 * Created by user on 22.11.2017.
 */

abstract class NetworkChangeReceiver @JvmOverloads constructor(ignoreInitialStickyBroadcast: Boolean = false) : BroadcastReceiver() {

    private var ignoreInitialStickyBroadcast = false

    init {
        this.ignoreInitialStickyBroadcast = ignoreInitialStickyBroadcast
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (!TextUtils.isEmpty(intent.action)) {
            if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                if (this.ignoreInitialStickyBroadcast) {
                    if (!isInitialStickyBroadcast) {
                        networkConnectivity(isNetworkConnected(context))
                    }
                } else {
                    networkConnectivity(isNetworkConnected(context))
                }
            }
        }
    }

    abstract fun networkConnectivity(isConnected: Boolean)

    companion object {

        val networkIntentFilter: IntentFilter
            get() = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

        fun isNetworkConnected(context: Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return manager.activeNetworkInfo != null && manager.activeNetworkInfo.isConnected
        }
    }

}
