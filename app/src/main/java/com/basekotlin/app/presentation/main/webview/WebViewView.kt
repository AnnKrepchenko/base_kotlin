package com.basekotlin.app.presentation.main.webview

import com.basekotlin.app.presentation.View

/**
 * Created by ann on 3/6/18.
 */
interface WebViewView : View {
    fun loadWebView(url: String)
    fun attachCookie(token: String)
}