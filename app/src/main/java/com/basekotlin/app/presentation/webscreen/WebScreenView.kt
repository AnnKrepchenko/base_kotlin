package com.basekotlin.app.presentation.webscreen

import com.basekotlin.app.presentation.View


/**
 * Created by ann on 3/4/18.
 */
interface WebScreenView : View {
    fun showPage()
    fun attachCookie(token: String)

}