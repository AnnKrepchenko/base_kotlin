package com.basekotlin.app.core

import com.basekotlin.app.data.net.NetBridge
import com.basekotlin.app.data.prefs.Prefs
import com.basekotlin.app.presentation.NoInternetCallback
import com.basekotlin.app.utils.typeface.TypefaceManager
import java.util.concurrent.Executor

/**
 * Created by ann on 2/26/18.
 */
interface AppBridge {
    var noInternetCallback: NoInternetCallback

    fun getPrefs(): Prefs

    fun getRemoteProvider(): NetBridge

    fun getExecutor(): Executor

    fun getTypefaceManager(): TypefaceManager

}