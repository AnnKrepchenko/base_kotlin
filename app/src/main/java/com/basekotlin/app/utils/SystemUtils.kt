package com.basekotlin.app.utils

import java.util.UUID
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent



object SystemUtils {

    val uniqueID: String
        get() = UUID.randomUUID().toString()

    fun openInBrowser() {

    }

}
