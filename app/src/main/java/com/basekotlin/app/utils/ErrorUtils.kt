package com.basekotlin.app.utils

import com.basekotlin.app.data.net.RestClient
import com.basekotlin.app.domain.net.error.BaseError
import com.basekotlin.app.domain.net.error.DefaultNetErrorResponse
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtils {

    val UNAUTHORIZED_401 = 401
    val UNPROCESSABLE_ENTITY_422 = 422
    val SERVER_IS_NOT_AVAILABLE_500 = 500
    val SOCKET_TIME_OUT_EXC = -1
    val NO_INTERNET_EXC = 777

    fun parseDefaultError(throwable: Throwable, serverIsBusyText: String, socketText: String): BaseError<DefaultNetErrorResponse> {
        val srError = BaseError<DefaultNetErrorResponse>(null)
        if (throwable is HttpException) {
            srError.code = throwable.code()
            try {
                val jsonError = throwable.response().errorBody()!!.string()
                val error = RestClient.gson.fromJson(jsonError, DefaultNetErrorResponse::class.java)
                if (error != null) {
                    srError.data = error
                }
            } catch (e: Exception) {
                e.printStackTrace()
                srError.message = throwable.message
            }

            if (throwable.code() >= SERVER_IS_NOT_AVAILABLE_500) {
                srError.message = serverIsBusyText
            }
        } else if (throwable is SocketTimeoutException) {
            throwable.printStackTrace()
            srError.code = SOCKET_TIME_OUT_EXC
            srError.message = socketText
        } else if (throwable is UnknownHostException) {
            throwable.printStackTrace()
            srError.code = NO_INTERNET_EXC
            srError.message = socketText
        } else {
            throwable.printStackTrace()
            srError.code = 0
            srError.message = throwable.message
        }
        return srError
    }

}
