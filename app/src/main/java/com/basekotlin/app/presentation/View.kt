package com.basekotlin.app.presentation

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes

interface View : LifecycleOwner {

    fun provideContext(): Context

    fun showToast(message: String)

    fun showToast(@StringRes text: Int)

    fun showAlertDialog(message: String)

    fun startLoadingDialog()

    fun stopLoadingDialog()

    fun goToLoginScreen()


}
