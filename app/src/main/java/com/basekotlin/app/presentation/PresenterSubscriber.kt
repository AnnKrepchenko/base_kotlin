package com.basekotlin.app.presentation

import android.os.Bundle
import android.text.TextUtils
import com.basekotlin.app.R

import com.basekotlin.app.core.AppBridge
import com.basekotlin.app.presentation.base.BaseActivity
import com.basekotlin.app.utils.ErrorUtils

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

open class PresenterSubscriber<T : View>(protected var app: AppBridge) {

    /**
     * Use [.getView] instead direct call view
     */
    private var view: T? = null
    private var isActive = false
    private var actionComplete = false

    val defaultErrorConsumer: Consumer<Throwable>
        get() = Consumer {
            actionComplete = true
            getView(object : ViewConsumer<T> {
                override fun consume(t: T) {
                    view?.stopLoadingDialog()
                }
            })
            handleDefaultNetError(it)
        }

    val defaultOnSubscribeConsumer: Consumer<Disposable>
        get() = Consumer {
            actionComplete = false
            getView(object : ViewConsumer<T> {
                override fun consume(t: T) {
                    view?.startLoadingDialog()
                }
            })
        }

    val defaultActionOnComplete: Action
        get() = Action {
            actionComplete = true
            getView(object : ViewConsumer<T> {
                override fun consume(t: T) {
                    view?.stopLoadingDialog()
                }
            })
        }

    fun bindView(view: T) {
        this.view = view
        this.isActive = true
    }

    fun onStart() {
        this.isActive = true
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        this.isActive = true
    }

    fun onResume() {
        this.isActive = true

        if (this.actionComplete) {
            getView(object : ViewConsumer<T> {
                override fun consume(t: T) {
                    view?.stopLoadingDialog()
                    actionComplete = false
                }

            })
        }
    }

    fun onPause() {
        this.isActive = true
    }

    fun onSaveInstanceState(outState: Bundle) {
        this.isActive = false
    }

    fun onStop() {
        this.isActive = false
    }

    fun unbindView() {
        this.view = null
        this.isActive = false
    }

    fun onDestroy() {
        this.view = null
        this.isActive = false
    }

    protected fun getView(consumer: ViewConsumer<T>?) {
        if (this.view != null && this.isActive) {
            consumer?.consume(this.view!!)
        }
    }


    private fun handleDefaultNetError(throwable: Throwable) {
        getView(object : ViewConsumer<T> {
            override fun consume(view: T) {
                val serverIsBusyText = view.provideContext().getString(R.string.server_is_currently_busy)
                val socketTimeOutText = view.provideContext().getString(R.string.socket_time_out_text)
                val error = ErrorUtils.parseDefaultError(throwable, serverIsBusyText, socketTimeOutText)
                when (error.code) {
                    ErrorUtils.UNAUTHORIZED_401 -> view.goToLoginScreen()
                    ErrorUtils.UNPROCESSABLE_ENTITY_422 -> if (error.data != null) {
                        view.showAlertDialog(error.data!!.errorMessages!![0].message!!)
                    }
                    ErrorUtils.SOCKET_TIME_OUT_EXC -> view.showToast(error.message!!)
                    ErrorUtils.NO_INTERNET_EXC -> {
                    }
                    else -> view.showToast(R.string.server_is_currently_busy)
                }
            }
        })
    }


}
