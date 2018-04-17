package com.basekotlin.app.presentation.main.webview

interface URLChangedInterface {
    fun onUrlChange(url: String)
    fun handleOtherUrl(url: String?)
}