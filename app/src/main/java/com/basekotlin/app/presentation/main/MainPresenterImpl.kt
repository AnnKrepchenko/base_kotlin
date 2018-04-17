package com.basekotlin.app.presentation.main

import android.text.TextUtils
import com.basekotlin.app.core.AppBridge
import com.basekotlin.app.presentation.PresenterSubscriber
import com.basekotlin.app.presentation.ViewConsumer
import com.basekotlin.app.utils.typeface.TypefaceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by ann on 2/27/18.
 */
class MainPresenterImpl(app: AppBridge) : MainPresenter<MainView>, PresenterSubscriber<MainView>(app) {

    override fun getTypefaceManager(): TypefaceManager {
        return app.getTypefaceManager()
    }
}