package com.basekotlin.app.utils.typeface

import android.content.Context
import android.graphics.Typeface

class TypefaceManager(context: Context) {

    val typefaceMontserratBlack: Typeface
    val typefaceMontserratBlackItalic: Typeface
    val typefaceMontserratBold: Typeface
    val typefaceMontserratBoldItalic: Typeface
    val typefaceMontserratExtraBold: Typeface
    val typefaceMontserratExtraBoldItalic: Typeface
    val typefaceMontserratExtraLight: Typeface
    val typefaceMontserratExtraLightItalic: Typeface
    val typefaceMontserratItalic: Typeface
    val typefaceMontserratLight: Typeface
    val typefaceMontserratLightItalic: Typeface
    val typefaceMontserratMedium: Typeface
    val typefaceMontserratMediumItalic: Typeface
    val typefaceMontserratRegular: Typeface
    val typefaceMontserratSemiBold: Typeface
    val typefaceMontserratSemiBoldItalic: Typeface
    val typefaceMontserratThin: Typeface
    val typefaceMontserratThinItalic: Typeface

    init {
        typefaceMontserratBlack = Typeface.createFromAsset(context.assets, MONTSERRAT_BLACK)
        typefaceMontserratBlackItalic = Typeface.createFromAsset(context.assets, MONTSERRAT_BLACK_ITALIC)
        typefaceMontserratBold = Typeface.createFromAsset(context.assets, MONTSERRAT_BOLD)
        typefaceMontserratBoldItalic = Typeface.createFromAsset(context.assets, MONTSERRAT_BOLD_ITALIC)
        typefaceMontserratExtraBold = Typeface.createFromAsset(context.assets, MONTSERRAT_EXTRA_BOLD)
        typefaceMontserratExtraBoldItalic = Typeface.createFromAsset(context.assets, MONTSERRAT_EXTRA_BOLD_ITALIC)
        typefaceMontserratExtraLight = Typeface.createFromAsset(context.assets, MONTSERRAT_EXTRA_LIGHT)
        typefaceMontserratExtraLightItalic = Typeface.createFromAsset(context.assets, MONTSERRAT_EXTRA_LIGHT_ITALIC)
        typefaceMontserratItalic = Typeface.createFromAsset(context.assets, MONTSERRAT_ITALIC)
        typefaceMontserratLight = Typeface.createFromAsset(context.assets, MONTSERRAT_LIGHT)
        typefaceMontserratLightItalic = Typeface.createFromAsset(context.assets, MONTSERRAT_LIGHT_ITALIC)
        typefaceMontserratMedium = Typeface.createFromAsset(context.assets, MONTSERRAT_MEDIUM)
        typefaceMontserratMediumItalic = Typeface.createFromAsset(context.assets, MONTSERRAT_MEDIUM_ITALIC)
        typefaceMontserratRegular = Typeface.createFromAsset(context.assets, MONTSERRAT_REGULAR)
        typefaceMontserratSemiBold = Typeface.createFromAsset(context.assets, MONTSERRAT_SEMI_BOLD)
        typefaceMontserratSemiBoldItalic = Typeface.createFromAsset(context.assets, MONTSERRAT_SEMI_BOLD_ITALIC)
        typefaceMontserratThin = Typeface.createFromAsset(context.assets, MONTSERRAT_THIN)
        typefaceMontserratThinItalic = Typeface.createFromAsset(context.assets, MONTSERRAT_THIN_ITALIC)

    }

    companion object {
        private val MONTSERRAT_BLACK = "fonts/Montserrat-Black.otf"
        private val MONTSERRAT_BLACK_ITALIC = "fonts/Montserrat-BlackItalic.otf"
        private val MONTSERRAT_BOLD = "fonts/Montserrat-Bold.otf"
        private val MONTSERRAT_BOLD_ITALIC = "fonts/Montserrat-BoldItalic.otf"
        private val MONTSERRAT_EXTRA_BOLD = "fonts/Montserrat-ExtraBold.otf"
        private val MONTSERRAT_EXTRA_BOLD_ITALIC = "fonts/Montserrat-ExtraBoldItalic.otf"
        private val MONTSERRAT_EXTRA_LIGHT = "fonts/Montserrat-ExtraLight.otf"
        private val MONTSERRAT_EXTRA_LIGHT_ITALIC = "fonts/Montserrat-ExtraLightItalic.otf"
        private val MONTSERRAT_ITALIC = "fonts/Montserrat-Italic.otf"
        private val MONTSERRAT_LIGHT = "fonts/Montserrat-Light.otf"
        private val MONTSERRAT_LIGHT_ITALIC = "fonts/Montserrat-LightItalic.otf"
        private val MONTSERRAT_MEDIUM = "fonts/Montserrat-Medium.otf"
        private val MONTSERRAT_MEDIUM_ITALIC = "fonts/Montserrat-MediumItalic.otf"
        private val MONTSERRAT_REGULAR = "fonts/Montserrat-Regular.otf"
        private val MONTSERRAT_SEMI_BOLD = "fonts/Montserrat-SemiBold.otf"
        private val MONTSERRAT_SEMI_BOLD_ITALIC = "fonts/Montserrat-SemiBoldItalic.otf"
        private val MONTSERRAT_THIN = "fonts/Montserrat-Thin.otf"
        private val MONTSERRAT_THIN_ITALIC = "fonts/Montserrat-ThinItalic.otf"

    }
}