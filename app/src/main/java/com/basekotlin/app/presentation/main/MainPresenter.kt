package com.basekotlin.app.presentation.main

import com.basekotlin.app.presentation.Presenter
import com.basekotlin.app.presentation.View
import com.basekotlin.app.utils.typeface.TypefaceManager

/**
 * Created by ann on 2/27/18.
 */
interface MainPresenter<in T : View> : Presenter<T> {
    fun getTypefaceManager(): TypefaceManager
}