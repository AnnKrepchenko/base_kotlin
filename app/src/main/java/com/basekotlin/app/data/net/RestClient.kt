package com.basekotlin.app.data.net

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.basekotlin.app.BuildConfig
import com.basekotlin.app.data.prefs.Prefs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {


    val gson: Gson

    init {
        gson = createGson()
    }

    fun createRetrofit(baseUrl: String, prefs: Prefs): Retrofit {
        return Retrofit.Builder()
                .client(createHttpClient(prefs))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
    }

    private fun createLogger(): Interceptor {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return logInterceptor
    }

    private fun createHttpClient(prefs: Prefs): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.LOG_ENABLED) {//NOT always true, he is lying
            okHttpClientBuilder.addNetworkInterceptor(createLogger())
        }
        okHttpClientBuilder.readTimeout(1, TimeUnit.MINUTES)
        okHttpClientBuilder.connectTimeout(1, TimeUnit.MINUTES)
        okHttpClientBuilder.writeTimeout(1, TimeUnit.MINUTES)
        okHttpClientBuilder.retryOnConnectionFailure(true)

        okHttpClientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            val token = prefs.getToken()
            if (!TextUtils.isEmpty(token)) {
                requestBuilder.addHeader("Authorization", token)
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return okHttpClientBuilder.build()
    }

    private fun createGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss")
        gsonBuilder.setLenient()
        return gsonBuilder.create()
    }

}
