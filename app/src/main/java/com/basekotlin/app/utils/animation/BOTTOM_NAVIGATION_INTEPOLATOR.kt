package com.basekotlin.app.utils.animation

import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.View

/**
 * Created by ann on 3/25/18.
 */
val INTERPOLATOR = LinearOutSlowInInterpolator()

fun <V : View> from(view: V): BottomNavigationBehavior<V> {
    val params = view.layoutParams as? CoordinatorLayout.LayoutParams
            ?: throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
    val behavior = params
            .behavior as? BottomNavigationBehavior<*> ?: throw IllegalArgumentException(
            "The view is not associated with BottomNavigationBehavior")
    return behavior as BottomNavigationBehavior<V>
}