package com.basekotlin.app.presentation.base

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity


open class BaseFragmentActivity : AppCompatActivity() {

    private val fragmentContainerResId: Int
        get() = android.R.id.content

    protected val rootContainerResId: Int
        get() = android.R.id.content

    protected fun addFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(fragmentContainerResId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commitAllowingStateLoss()

    }

    protected fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragmentContainerResId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commitAllowingStateLoss()
    }

    protected fun removeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.commitAllowingStateLoss()
    }

}
