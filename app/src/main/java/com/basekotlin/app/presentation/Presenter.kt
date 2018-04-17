package com.basekotlin.app.presentation

import android.os.Bundle

interface Presenter<in T : View> {

    fun bindView(view: T)

    fun unbindView()

    fun onDestroy()

    fun onRestoreInstanceState(savedInstanceState: Bundle)

    fun onResume()

    fun onSaveInstanceState(outState: Bundle)

    fun onStop()

    fun onPause()

    fun onStart()
}
