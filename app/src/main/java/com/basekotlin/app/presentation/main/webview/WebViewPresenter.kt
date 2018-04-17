package com.basekotlin.app.presentation.main.webview

import com.basekotlin.app.presentation.Presenter
import com.basekotlin.app.presentation.View

/**
 * Created by ann on 3/6/18.
 */
interface WebViewPresenter<in T : View> : Presenter<T> {
    fun load(teamValue: String?)
    fun getCookie()
}