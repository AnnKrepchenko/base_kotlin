package com.basekotlin.app.data.net

import com.basekotlin.app.domain.net.request.LoginRequest
import com.basekotlin.app.domain.net.response.LoginResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by ann on 2/26/18.
 */
interface API {
    @POST("auth")
    fun login(@Body registerRequest: LoginRequest): Observable<LoginResponse>

}