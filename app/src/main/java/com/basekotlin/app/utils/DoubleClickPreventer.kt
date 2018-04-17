package com.basekotlin.app.utils

import android.os.SystemClock
import android.support.annotation.IdRes
import android.support.v4.util.LongSparseArray

object DoubleClickPreventer {

    val MIN_CLICK_INTERVAL_100_MS: Long = 100
    val MIN_CLICK_INTERVAL_200_MS: Long = 200
    val MIN_CLICK_INTERVAL_500_MS: Long = 500
    val MAX_CLICL_INTERVAL_NO_RESPONSE_5000_MS: Long = 5000

    private val timeClick = LongSparseArray<Long>()

    fun onClick(@IdRes resId: Int, listener: OnNextListener) {
        if (timeClick.get(resId.toLong()) == null) {
            timeClick.put(resId.toLong(), SystemClock.uptimeMillis())
            listener.onNext()
            return
        }

        if (SystemClock.uptimeMillis() - timeClick.get(resId.toLong()) < MIN_CLICK_INTERVAL_200_MS) {
            timeClick.put(resId.toLong(), SystemClock.uptimeMillis())
        } else {
            timeClick.put(resId.toLong(), SystemClock.uptimeMillis())
            listener.onNext()
        }
    }

    fun onClickWithCustomInterval(operationId: Long, listener: OnNextListener, minClickInterval: Long) {
        if (timeClick.get(operationId) == null) {
            timeClick.put(operationId, SystemClock.uptimeMillis())
            listener.onNext()
            return
        }

        if (SystemClock.uptimeMillis() - timeClick.get(operationId) < minClickInterval) {
            timeClick.put(operationId, SystemClock.uptimeMillis())
        } else {
            timeClick.put(operationId, SystemClock.uptimeMillis())
            listener.onNext()
        }
    }

    interface OnNextListener {
        fun onNext()
    }
}
