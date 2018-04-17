package com.basekotlin.app.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.support.design.internal.BaselineLayout
import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.BottomNavigationView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.basekotlin.app.core.BaseApp
import java.lang.reflect.Field

class ExtendedBottomNavigationView(context: Context, attrs: AttributeSet) : BottomNavigationView(context, attrs) {
    private var fontFace: Typeface? = null

    init {
        fontFace = BaseApp.provideApp(context).getTypefaceManager().typefaceMontserratRegular
    }

    @SuppressLint("RestrictedApi")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val bottomMenu = getChildAt(0) as ViewGroup
        val bottomMenuChildCount = bottomMenu.childCount
        var item: BottomNavigationItemView
        var itemTitle: View
        val shiftingMode: Field

        try {
            shiftingMode = bottomMenu.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(bottomMenu, false)
            shiftingMode.isAccessible = false
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        for (i in 0 until bottomMenuChildCount) {
            item = bottomMenu.getChildAt(i) as BottomNavigationItemView
            //this shows all titles of items
            item.setChecked(true)
            //every BottomNavigationItemView has two children, first is an itemIcon and second is an itemTitle
            itemTitle = item.getChildAt(1)
            //every itemTitle has two children, first is a smallLabel and second is a largeLabel. these two are type of AppCompatTextView
            ((itemTitle as BaselineLayout).getChildAt(0) as TextView).typeface = fontFace
            // ((itemTitle).getChildAt(0) as TextView).textSize = resources.getDimension(R.dimen.bn_textsize)
            (itemTitle.getChildAt(1) as TextView).typeface = fontFace
            //    (itemTitle.getChildAt(1) as TextView).textSize = resources.getDimension(R.dimen.bn_textsize)
        }
    }


}