package com.basekotlin.app.utils

import android.webkit.CookieManager
import com.basekotlin.app.BuildConfig

object WebViewUtils {
    fun attachCoockies(cookieManager: CookieManager, token: String) {
        cookieManager.removeAllCookies({ _ ->
            cookieManager.setAcceptCookie(true)
            cookieManager.setCookie(BuildConfig.WEB_BASE_URL, "Authorization=$token")
            cookieManager.setCookie(BuildConfig.WEB_BASE_URL, "enabled-mobile=true")
        })
    }
}