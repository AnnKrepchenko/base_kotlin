package com.basekotlin.app.presentation.webscreen

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.basekotlin.app.R
import com.basekotlin.app.core.BaseApp
import com.basekotlin.app.presentation.base.BaseActivity
import com.basekotlin.app.utils.WebViewUtils
import kotlinx.android.synthetic.main.activity_webview.*


/**
 * Created by ann on 3/4/18.
 */
class WebScreenActivity : BaseActivity<WebScreenPresenter<WebScreenView>, WebScreenView>(), WebScreenView {


    override val layoutRes: Int
        get() = R.layout.activity_webview
    var url: String? = null

    override fun providePresenter(): WebScreenPresenter<WebScreenView> {
        return WebScreenPresenterImpl(BaseApp.provideApp(this))
    }

    override fun provideView(): WebScreenView {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent?.extras?.getString(KEY_URL)
        presenter?.getCookie()

    }

    override fun initViews() {
        wvWebView.setCookiesEnabled(true)
        wvWebView.webViewClient = ZeroWebViewClient()
    }

    override fun setFonts() {
    }

    override fun attachCookie(token: String) {
        WebViewUtils.attachCoockies(CookieManager.getInstance(), token)
        showPage()
    }

    override fun showPage() {
        wvWebView.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        wvWebView.onDestroy()
    }

    inner class ZeroWebViewClient : WebViewClient() {
        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            Log.w("method", "onLoadResource")

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            stopLoadingDialog()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            startLoadingDialog()
            Log.w("method", "onPageStarted")

        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            Log.w("method", "onReceivedError")
        }
    }

    companion object {
        val KEY_URL = "url"
    }
}