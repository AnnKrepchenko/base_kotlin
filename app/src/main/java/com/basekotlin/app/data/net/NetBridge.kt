package com.basekotlin.app.data.net

import com.basekotlin.app.domain.net.response.LoginResponse
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * Created by ann on 2/26/18.
 */
interface NetBridge {
    fun setNetworkState(isConnected: Boolean)

    fun login(email: String, password: String): Observable<LoginResponse>
}