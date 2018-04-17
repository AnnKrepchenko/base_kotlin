package com.basekotlin.app.utils

import android.view.MotionEvent
import android.view.View

/**
 * Created by user on 31.10.2017.
 */

class CustomTouchListener : View.OnTouchListener {
    private var initialTouchX: Float = 0.toFloat()
    private var initialTouchY: Float = 0.toFloat()
    private var lastTouchDown: Long = 0
    private var listener: OnClickListener? = null

    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchDown = System.currentTimeMillis()
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                return true
            }
            MotionEvent.ACTION_UP -> {
                val Xdiff = (event.rawX - initialTouchX).toInt()
                val Ydiff = (event.rawY - initialTouchY).toInt()
                if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD && Xdiff < MAX_DIFF && Ydiff < MAX_DIFF) {
                    if (this.listener != null) {
                        this.listener!!.onClicked(v)
                    }
                }
                return true
            }
        }
        return false
    }

    interface OnClickListener {
        fun onClicked(view: View)
    }

    companion object {

        private val MAX_DIFF = 10
        private val CLICK_ACTION_THRESHHOLD = 100
    }

}
