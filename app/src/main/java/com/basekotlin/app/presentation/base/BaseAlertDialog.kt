package com.basekotlin.app.presentation.base

/**
 * Created by ann on 7/26/17.
 */


import android.content.Context
import android.content.DialogInterface
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v4.content.res.ResourcesCompat
import android.text.SpannableString
import android.text.TextUtils
import android.widget.Button
import com.basekotlin.app.R
import com.basekotlin.app.utils.typeface.TypefaceManager

abstract class BaseAlertDialog : android.app.AlertDialog {

    protected constructor(context: Context) : super(context) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {}

    protected constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId) {}

    class Builder : android.app.AlertDialog.Builder {

        private var typefaceManager: TypefaceManager? = null
        private var buttonColor = -1

        constructor(context: Context, @ColorRes buttonColor: Int) : super(context) {
            this.buttonColor = buttonColor
            //   this.typefaceManager = SrApp.provideApp(context).getTypefaceManager()
        }

        override fun setTitle(title: CharSequence): android.app.AlertDialog.Builder {
            val string = SpannableString(title)
            // string.setSpan(TypefaceSpan(typefaceManager!!.getTypefaceDemi()), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return super.setTitle(string)
        }

        override fun setTitle(@StringRes titleId: Int): android.app.AlertDialog.Builder {
            val title = context.getString(titleId)
            return setTitle(title)
        }

        override fun setMessage(message: CharSequence?): android.app.AlertDialog.Builder {
            if (TextUtils.isEmpty(message)) {
                return super.setMessage("")
            }
            val string = SpannableString(message)
            //  string.setSpan(TypefaceSpan(typefaceManager!!.getTypefaceReg()), 0, message!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return super.setMessage(string)
        }

        override fun setMessage(@StringRes messageId: Int): android.app.AlertDialog.Builder {
            val message = context.getString(messageId)
            return setMessage(message)
        }

        override fun show(): android.app.AlertDialog {
            val alertDialog = create()
            alertDialog.setOnShowListener {
                val color = ResourcesCompat.getColor(context.resources,
                        if (buttonColor == -1) R.color.colorAccent else buttonColor,
                        context.theme)
                var button: Button?
                val buttons = intArrayOf(DialogInterface.BUTTON_NEGATIVE, DialogInterface.BUTTON_NEUTRAL, DialogInterface.BUTTON_POSITIVE)
                for (buttonType in buttons) {
                    button = alertDialog.getButton(buttonType)
                    if (button != null) {
                        button.setTextColor(color)
                        //  button.typeface = typefaceManager!!.getTypefaceDemi()
                    }
                }
            }
            alertDialog.show()
            return alertDialog
        }
    }
}
