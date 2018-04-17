package com.basekotlin.app.presentation.main.webview

import com.basekotlin.app.core.AppBridge
import com.basekotlin.app.presentation.PresenterSubscriber
import com.basekotlin.app.presentation.ViewConsumer

/**
 * Created by ann on 3/6/18.
 */
class WebViewPresenterImpl(app: AppBridge) : WebViewPresenter<WebViewView>, PresenterSubscriber<WebViewView>(app) {
    override fun load(teamValue: String?) {
        if (teamValue != null) {
            getView(object : ViewConsumer<WebViewView> {
                override fun consume(t: WebViewView) {
                    t.loadWebView("")
                }
            })
        }
    }

    override fun getCookie() {
        getView(object : ViewConsumer<WebViewView> {
            override fun consume(t: WebViewView) {
                t.attachCookie(app.getPrefs().getToken()!!)
            }
        })
    }
}