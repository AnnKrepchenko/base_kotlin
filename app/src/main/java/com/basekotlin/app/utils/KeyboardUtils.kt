package com.basekotlin.app.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager


import java.util.HashMap

/**
 * Created by user on 25.10.2017.
 */
class KeyboardUtils private constructor(activity: Activity, private var mCallback: SoftKeyboardToggleListener?) : ViewTreeObserver.OnGlobalLayoutListener {
    private val mRootView: View


    init {
        mRootView = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        mRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val r = Rect()
        mRootView.getWindowVisibleDisplayFrame(r)
        val screenHeight = mRootView.rootView.height
        val heightDiff = screenHeight - (r.bottom - r.top)
        visible = heightDiff > screenHeight / KEYBOARD_VISIBLE_PART
        if (mCallback != null) {
            mCallback!!.onToggleSoftKeyboard(visible)
        }
    }

    private fun removeListener() {
        mCallback = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        } else {
            mRootView.viewTreeObserver.removeGlobalOnLayoutListener(this)
        }
    }

    interface SoftKeyboardToggleListener {
        fun onToggleSoftKeyboard(isVisible: Boolean)
    }

    companion object {

        private val KEYBOARD_VISIBLE_PART = 3
        private val sListenerMap = HashMap<SoftKeyboardToggleListener, KeyboardUtils>()
        private var visible: Boolean = false

        fun addKeyboardToggleListener(act: Activity, listener: SoftKeyboardToggleListener) {
            removeKeyboardToggleListener(listener)
            sListenerMap.put(listener, KeyboardUtils(act, listener))
        }

        fun removeKeyboardToggleListener(listener: SoftKeyboardToggleListener) {
            if (sListenerMap.containsKey(listener)) {
                val k = sListenerMap[listener]
                k?.removeListener()
                sListenerMap.remove(listener)
            }
        }

        fun removeAllKeyboardToggleListeners() {
            for (l in sListenerMap.keys) {
                sListenerMap[l]?.removeListener()
            }
            sListenerMap.clear()
        }

        fun hideKeyboard(context: Context?) {
            if (context != null && context is Activity) {
                val view = context.currentFocus
                hideKeyboard(view)
            }
        }

        fun hideKeyboard(activity: Activity?) {
            if (activity != null) {
                val view = activity.currentFocus
                hideKeyboard(view)
            }
        }

        fun hideKeyboard(view: View?) {
            if (view != null) {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) {
                    if (visible)
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }

        fun toggleKeyboard(activity: Activity?) {
            if (activity != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) {
                    if (imm.isActive) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                    } else {
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY)
                    }
                }
            }
        }

        fun showKeyboard(view: View?) {
            if (view != null) {
                view.requestFocus()
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun openKeyboardHard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun hideKeyboardHard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null && visible) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            }
        }

        fun hideKeyboardAndRequestView(activity: Activity) {
            val view = activity.currentFocus ?: return
            KeyboardUtils.hideKeyboard(view.context)
            if (view.context is OnFocusChangeRequestListener) {
                (view.context as OnFocusChangeRequestListener).onFocusChangeRequest()
            }
        }

        fun hideKeyboardAndRequestView(view: View?) {
            if (view == null) {
                return
            }
            KeyboardUtils.hideKeyboard(view.context)
            if (view.context is OnFocusChangeRequestListener) {
                (view.context as OnFocusChangeRequestListener).onFocusChangeRequest()
            }
        }
    }

}
