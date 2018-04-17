package com.basekotlin.app.utils

import android.os.Handler
import android.os.Looper

import java.util.Timer
import java.util.TimerTask


class TimerUtil {

    private var elapsedTime: Long = 0
    private var timerTask: TimerTask? = null
    private var prevTimestamp: Long = 0
    private var delta: Long = 0
    private var timer: Timer? = null
    private val step: Long = 10
    private var listener: TickListener? = null
    private val mainHandler: Handler

    interface TickListener {
        fun onMsTick(elapsedTime: Long)

        fun onSecTick(elapsedTime: Long)
    }

    init {
        mainHandler = Handler(Looper.getMainLooper())
    }

    private fun initTimerTask() {
        prevTimestamp = System.currentTimeMillis()
        timerTask = object : TimerTask() {
            override fun run() {
                delta = System.currentTimeMillis() - prevTimestamp
                elapsedTime += delta
                prevTimestamp += delta
                if (listener != null) {
                    postMsToMainThread(elapsedTime)

                    if (elapsedTime % 1000 <= 30) { // to avoid frequent redrawing
                        postSecToMainThread(elapsedTime)
                    }

                }
            }
        }
    }

    fun setListener(listener: TickListener) {
        this.listener = listener
    }

    fun start() {
        initTimerTask()
        timer = Timer()
        timer!!.schedule(timerTask, 1, step)

    }

    fun pause() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    fun stop() {
        if (timer != null) {
            timer!!.purge()
        }
        elapsedTime = 0
    }

    private fun postSecToMainThread(elapsedTime: Long) {
        mainHandler.post {
            if (listener != null) {
                listener!!.onSecTick(elapsedTime)
            }
        }
    }

    private fun postMsToMainThread(elapsedTime: Long) {
        mainHandler.post {
            if (listener != null) {
                listener!!.onMsTick(elapsedTime)
            }
        }
    }

}
