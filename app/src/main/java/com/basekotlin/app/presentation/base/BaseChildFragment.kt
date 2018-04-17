package com.basekotlin.app.presentation.base

import android.support.v4.app.Fragment

import com.basekotlin.app.utils.OnFocusChangeRequestListener


open class BaseChildFragment : Fragment(), OnFocusChangeRequestListener {

    protected val fragmentContainerResId: Int
        get() = android.R.id.content

    protected fun addFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(fragmentContainerResId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commitAllowingStateLoss()

    }

    protected fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(fragmentContainerResId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commitAllowingStateLoss()
    }

    /**
     * override this method if kb should be closed and edittext unfocused
     * use @KeyboardUtils.hideKeyboardAndRequestView() to call this method
     * View must contain tags:
     * android:focusable="true"
     * android:focusableInTouchMode="true"
     */
    override fun onFocusChangeRequest() {
        //call view.requestFocus();
    }
}
