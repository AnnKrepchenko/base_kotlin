package com.basekotlin.app.presentation.webscreen

import com.basekotlin.app.BuildConfig
import com.basekotlin.app.core.AppBridge
import com.basekotlin.app.presentation.PresenterSubscriber
import com.basekotlin.app.presentation.ViewConsumer

/**
 * Created by ann on 3/4/18.
 */
class WebScreenPresenterImpl(app: AppBridge) : WebScreenPresenter<WebScreenView>, PresenterSubscriber<WebScreenView>(app) {
    override fun getCookie() {
        getView(object : ViewConsumer<WebScreenView> {
            override fun consume(t: WebScreenView) {
                t.attachCookie(app.getPrefs().getToken()!!)
            }
        })
    }
}