package com.basekotlin.app.domain.net.error

data class BaseError<T>(var data: T? = null) {

    var code: Int = 0
    var message: String? = null
}
