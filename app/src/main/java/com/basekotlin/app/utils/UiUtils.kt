package com.basekotlin.app.utils

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.basekotlin.app.R
import android.R.attr.data
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.TypedValue
import android.view.ViewGroup


/**
 * Created by ann on 3/4/18.
 */
object UiUtils {

    /* use this for setup avatar which displays on white background */
    fun loadAvatarWithGrayLightPlaceholder(imageView: ImageView?, url: String?) {
        if (imageView != null) {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(imageView.context)
                        .load(url)
                        .apply(RequestOptions().placeholder(R.drawable.user_placeholder_downloading_gray_light))
                        .apply(RequestOptions().error(R.drawable.user_placeholder_gray))
                        .into(imageView)
            }
        }
    }

    /* use this for setup avatar which displays on white background */
    fun loadLogoWithGrayLightPlaceholder(imageView: ImageView?, url: String?) {
        if (imageView != null) {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(imageView.context)
                        .load(url)
                        .apply(RequestOptions().placeholder(R.drawable.user_placeholder_downloading_gray_light))
                        .into(imageView)
            } else {
                imageView.visibility = View.INVISIBLE
            }
        }
    }

    fun getActionBarSize(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        } else {
            0
        }
    }

    fun getCoordinatorParams(bottomOffset: Int): CoordinatorLayout.LayoutParams {
        val params = CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.setMargins(0, 0, 0, bottomOffset)
        return params
    }

}