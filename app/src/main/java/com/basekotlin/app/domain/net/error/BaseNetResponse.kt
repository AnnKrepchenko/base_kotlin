package com.basekotlin.app.domain.net.error

open class BaseNetResponse<T> {

    var isStatus: Boolean = false
    var data: T? = null
}
