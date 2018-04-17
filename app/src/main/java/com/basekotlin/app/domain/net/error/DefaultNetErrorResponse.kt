package com.basekotlin.app.domain.net.error

import com.google.gson.annotations.SerializedName

open class DefaultNetErrorResponse {

    @SerializedName("error_messages")
    var errorMessages: MutableList<ErrorMessage>? = null
}
