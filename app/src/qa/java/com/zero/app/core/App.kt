package com.zero.app.core

import android.webkit.WebView
import com.basekotlin.app.core.BaseApp

/**
 * Created by ann on 2/26/18.
 */
class App : BaseApp() {


    override fun onCreate() {
        super.onCreate()
        WebView.setWebContentsDebuggingEnabled(true)
    }
}