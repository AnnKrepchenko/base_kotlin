package com.basekotlin.app.presentation

/**
 * Created by user on 07.11.2017.
 */

interface ViewConsumer<in T> {
    fun consume(t: T)
}
