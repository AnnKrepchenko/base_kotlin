package com.basekotlin.app.utils.animation

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator

class BottomNavigationBehavior<V : View> : VerticalScrollingBehavior<V> {
    private val mWithSnackBarImpl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) LollipopBottomNavWithSnackBarImpl() else PreLollipopBottomNavWithSnackBarImpl()
    private var attrsArray = intArrayOf(android.R.attr.id)
    private var mTabLayoutId: Int? = null
    private var hidden: Boolean = false
    private var mOffsetValueAnimator: ViewPropertyAnimatorCompat? = null
    private var mTabLayout: ViewGroup? = null
    private var mTabsHolder: View? = null
    private var mSnackbarHeight = -1
    var isScrollingEnabled = true
    private var hideAlongSnackbar: Boolean = false

    constructor() : super() {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs,
                attrsArray)
        mTabLayoutId = a.getResourceId(0, View.NO_ID)
        a.recycle()
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: V?, dependency: View?): Boolean {
        mWithSnackBarImpl.updateSnackbar(parent, dependency, child)
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout?, child: V?, dependency: View?) {
        updateScrollingForSnackbar(dependency, child, true)
        super.onDependentViewRemoved(parent, child, dependency)
    }

    private fun updateScrollingForSnackbar(dependency: View?, child: V?, enabled: Boolean) {
        if (dependency is Snackbar.SnackbarLayout) {
            isScrollingEnabled = enabled
            if (!hideAlongSnackbar && ViewCompat.getTranslationY(child!!) != 0f) {
                ViewCompat.setTranslationY(child, 0f)
                hidden = false
                hideAlongSnackbar = true
            } else if (hideAlongSnackbar) {
                hidden = true
                animateOffset(child, -child!!.height)
            }
        }
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: V?, dependency: View?): Boolean {
        updateScrollingForSnackbar(dependency, child, false)
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onLayoutChild(parent: CoordinatorLayout?, child: V?, layoutDirection: Int): Boolean {
        val layoutChild = super.onLayoutChild(parent, child, layoutDirection)
        if (mTabLayout == null && mTabLayoutId != View.NO_ID) {
            mTabLayout = findTabLayout(child!!)
            getTabsHolder()
        }

        return layoutChild
    }

    private fun findTabLayout(child: View): ViewGroup? {
        return if (mTabLayoutId == 0) null else child.findViewById<View>(mTabLayoutId!!) as ViewGroup
    }

    override fun onNestedVerticalOverScroll(coordinatorLayout: CoordinatorLayout, child: V, @ScrollDirection direction: Int, currentOverScroll: Int, totalOverScroll: Int) {}

    override fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, @ScrollDirection scrollDirection: Int) {
        // handleDirection(child, scrollDirection); -> to block hiding bottom bar on scroll
    }

    private fun handleDirection(child: V, @ScrollDirection scrollDirection: Int) {
        if (!isScrollingEnabled) return
        if (scrollDirection == VerticalScrollingBehavior.ScrollDirection.SCROLL_DIRECTION_DOWN && hidden) {
            hidden = false
            animateOffset(child, 0)
        } else if (scrollDirection == VerticalScrollingBehavior.ScrollDirection.SCROLL_DIRECTION_UP && !hidden) {
            hidden = true
            animateOffset(child, child.height)
        }
    }

    override fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float, @ScrollDirection scrollDirection: Int): Boolean {
        //  handleDirection(child, scrollDirection); -> to block hiding bottom bar on scroll
        return true
    }

    private fun animateOffset(child: V?, offset: Int) {
        ensureOrCancelAnimator(child)
        mOffsetValueAnimator!!.translationY(offset.toFloat()).start()
        animateTabsHolder(offset)
    }

    private fun animateTabsHolder(offset: Int) {
        var offset = offset
        if (mTabsHolder != null) {
            offset = if (offset > 0) 0 else 1
            ViewCompat.animate(mTabsHolder).alpha(offset.toFloat()).setDuration(200).start()
        }
    }

    private fun ensureOrCancelAnimator(child: V?) {
        if (mOffsetValueAnimator == null) {
            mOffsetValueAnimator = ViewCompat.animate(child)
            mOffsetValueAnimator!!.duration = 100
            mOffsetValueAnimator!!.interpolator = INTERPOLATOR
        } else {
            mOffsetValueAnimator!!.cancel()
        }
    }

    private fun getTabsHolder() {
        if (mTabLayout != null) {
            mTabsHolder = mTabLayout!!.getChildAt(0)
        }
    }

    fun setHidden(view: V, bottomLayoutHidden: Boolean) {
        if (!bottomLayoutHidden && hidden) {
            animateOffset(view, 0)
        } else if (bottomLayoutHidden && !hidden) {
            animateOffset(view, -view.height)
        }
        hidden = bottomLayoutHidden
    }

    private interface BottomNavigationWithSnackbar {
        fun updateSnackbar(parent: CoordinatorLayout?, dependency: View?, child: View?)
    }

    private inner class PreLollipopBottomNavWithSnackBarImpl : BottomNavigationWithSnackbar {

        override fun updateSnackbar(parent: CoordinatorLayout?, dependency: View?, child: View?) {
            if (dependency is Snackbar.SnackbarLayout) {
                if (mSnackbarHeight == -1) {
                    mSnackbarHeight = dependency.height
                }

                val targetPadding = child!!.measuredHeight

                val shadow = ViewCompat.getElevation(child).toInt()
                val layoutParams = dependency.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.bottomMargin = targetPadding - shadow
                child.bringToFront()
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    child.parent.requestLayout()
                    (child.parent as View).invalidate()
                }

            }
        }
    }

    private inner class LollipopBottomNavWithSnackBarImpl : BottomNavigationWithSnackbar {

        override fun updateSnackbar(parent: CoordinatorLayout?, dependency: View?, child: View?) {
            if (dependency is Snackbar.SnackbarLayout) {
                if (mSnackbarHeight == -1) {
                    mSnackbarHeight = dependency.height
                }
                val targetPadding = mSnackbarHeight + child!!.measuredHeight
                dependency.setPadding(dependency.paddingLeft,
                        dependency.paddingTop, dependency.paddingRight, targetPadding
                )
            }
        }
    }
}