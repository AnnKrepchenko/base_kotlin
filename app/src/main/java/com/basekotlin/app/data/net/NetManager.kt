package com.basekotlin.app.data.net

import com.basekotlin.app.BuildConfig
import com.basekotlin.app.data.prefs.Prefs
import com.basekotlin.app.domain.net.request.LoginRequest
import com.basekotlin.app.domain.net.response.LoginResponse
import io.reactivex.Observable

/**
 * Created by ann on 2/26/18.
 */
class NetManager(prefs: Prefs) : NetBridge {



    private var api: API
    private var isConnected = false

    init {
        val appRetrofit = RestClient.createRetrofit(BuildConfig.SERVER_API_URL + BuildConfig.API_VERSION, prefs)
        api = appRetrofit.create(API::class.java)!!
    }

    override fun login(email: String, password: String): Observable<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }


    override fun setNetworkState(isConnected: Boolean) {
        this.isConnected = isConnected
    }

}