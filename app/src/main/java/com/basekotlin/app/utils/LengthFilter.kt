package com.basekotlin.app.utils

import android.text.InputFilter
import android.text.Spanned

class LengthFilter(
        /**
         * @return the maximum length enforced by this input filter
         */
        val max: Int, limitChar: Char, private val limitCharListener: LimitCharListener?) : InputFilter {
    private var limitChar: Char = 0.toChar()

    init {
        this.limitChar = limitChar
    }

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned,
                        dstart: Int, dend: Int): CharSequence? {
        var keep = max - (dest.length - (dend - dstart))
        if (keep <= 0) {
            if (limitChar.toInt() != 0 && source.length != 0 && source[keep] == limitChar)
                limitCharListener?.onLimitCharFired()
            return ""
        } else if (keep >= end - start) {
            return null // keep original
        } else {
            keep += start
            if (Character.isHighSurrogate(source[keep - 1])) {
                --keep
                if (keep == start) {
                    return ""
                }
            }
            return source.subSequence(start, keep)
        }
    }

    interface LimitCharListener {
        fun onLimitCharFired()
    }
}
