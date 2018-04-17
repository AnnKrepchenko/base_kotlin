package com.basekotlin.app.presentation.webscreen

import com.basekotlin.app.presentation.Presenter
import com.basekotlin.app.presentation.View

/**
 * Created by ann on 3/4/18.
 */
interface WebScreenPresenter<in T : View> : Presenter<T> {
    fun getCookie()
}