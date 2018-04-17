package com.basekotlin.app.core

import android.webkit.WebView
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary

/**
 * Created by ann on 2/26/18.
 */
class App() : BaseApp() {


    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        WebView.setWebContentsDebuggingEnabled(true)
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }
}