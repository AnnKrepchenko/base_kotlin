package com.basekotlin.app.utils.animation

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.util.Log
import android.view.View

object BottomNavigationViewHelper {

    private var hidden: Boolean = false
    private var mOffsetValueAnimator: ViewPropertyAnimatorCompat? = null


    @SuppressLint("RestrictedApi")
    fun removeShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShiftingMode(false)
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData.isChecked)
            }

        } catch (e: NoSuchFieldException) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field")
        } catch (e: IllegalAccessException) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
        }
    }

    fun setHidden(view: BottomNavigationView, bottomLayoutHide: Boolean) {
        if (!bottomLayoutHide && hidden) {
            animateOffset(view, 0)
        } else if (bottomLayoutHide && !hidden) {
            animateOffset(view, view.height)
        }
        hidden = bottomLayoutHide
    }

    private fun animateOffset(child: BottomNavigationView?, offset: Int) {
        ensureOrCancelAnimator(child)
        mOffsetValueAnimator!!.translationY(offset.toFloat()).start()
        animateTabsHolder(child!!, offset)
    }

    private fun ensureOrCancelAnimator(child: View?) {
        if (mOffsetValueAnimator == null) {
            mOffsetValueAnimator = ViewCompat.animate(child)
            mOffsetValueAnimator!!.duration = 100
            mOffsetValueAnimator!!.interpolator = INTERPOLATOR
        } else {
            mOffsetValueAnimator!!.cancel()
        }
    }

    private fun animateTabsHolder(tabsHolder: View, offset: Int) {
        var offset = offset
        offset = if (offset > 0) 0 else 1
        ViewCompat.animate(tabsHolder).alpha(offset.toFloat()).setDuration(200).start()
    }

}