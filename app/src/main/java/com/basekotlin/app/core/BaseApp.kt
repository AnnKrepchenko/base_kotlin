package com.basekotlin.app.core

import android.app.Application
import android.content.Context
import com.basekotlin.app.data.net.NetBridge
import com.basekotlin.app.data.net.NetManager
import com.basekotlin.app.data.prefs.PreferencesManager
import com.basekotlin.app.data.prefs.Prefs
import com.basekotlin.app.presentation.NoInternetCallback
import com.basekotlin.app.utils.NetworkChangeReceiver
import com.basekotlin.app.utils.typeface.TypefaceManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by ann on 2/26/18.
 */
open class BaseApp : Application(), AppBridge {

    private lateinit var netBridge: NetBridge
    private lateinit var prefs: Prefs
    private lateinit var executor: Executor
    private lateinit var typefaceManager: TypefaceManager
    override lateinit var noInternetCallback: NoInternetCallback


    override fun onCreate() {
        super.onCreate()
        executor = Executors.newFixedThreadPool(2)
        prefs = PreferencesManager(this)
        netBridge = NetManager(prefs)
        typefaceManager = TypefaceManager(this)
        registerReceiver(networkChangeReceiver, NetworkChangeReceiver.networkIntentFilter)
    }

    override fun getPrefs(): Prefs {
        return prefs
    }

    override fun getRemoteProvider(): NetBridge {
        return netBridge
    }

    override fun getExecutor(): Executor {
        return executor
    }

    override fun getTypefaceManager(): TypefaceManager {
        return typefaceManager
    }

    private val networkChangeReceiver = object : NetworkChangeReceiver() {
        override fun networkConnectivity(isConnected: Boolean) {
            netBridge.setNetworkState(isConnected)
            if (::noInternetCallback.isInitialized) {
                noInternetCallback.onNetworkConnectionChanged(isConnected)
            }
        }
    }

    companion object {
        fun provideApp(context: Context): AppBridge {
            return context.applicationContext as AppBridge
        }

    }

}