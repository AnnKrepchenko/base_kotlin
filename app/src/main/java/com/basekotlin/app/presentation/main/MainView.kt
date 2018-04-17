package com.basekotlin.app.presentation.main

import com.basekotlin.app.domain.net.model.User
import com.basekotlin.app.presentation.View

/**
 * Created by ann on 2/27/18.
 */
interface MainView : View {
    fun hideBottomBar()
    fun showBottomBar()
}