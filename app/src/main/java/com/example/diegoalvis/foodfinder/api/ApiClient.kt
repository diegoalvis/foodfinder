package com.diegoalvis.android.newsapp.api

import android.content.Context
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {

    const val CURRENT_SESSION = "current_session"
    const val AUTH_TOKEN = "auth_token"

    private val BASE_URL: String = "http://stg-api.pedidosya.com/public/v1/"
    private var retrofit: Retrofit? = null

    fun getInterface(context: Context): ApiInterface {
        if (retrofit == null) {
            retrofit = buildRetrofit(context)
        }
        return retrofit!!.create(ApiInterface::class.java)
    }

    @Synchronized
    private fun buildRetrofit(context: Context) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(getOkHttpClientTokenInstance(context))
        .build()

    private fun getOkHttpClientTokenInstance(context: Context) =
        OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(headersInterceptor(context))
            .build()


    private fun headersInterceptor(context: Context) = Interceptor { chain ->
        val original = chain.request()
        val authToken = context.getSharedPreferences(CURRENT_SESSION, Context.MODE_PRIVATE).getString(AUTH_TOKEN, "-")

        val request = original.newBuilder()
            .header("Authorization", authToken)
            .header("Content-Type", "application/json; charset=utf-8")

        val response = chain.proceed(request.build())

        if (authToken.isNullOrEmpty() || response.code() == 403) {
            getAuthToken(context)
        }
        response
    }

    fun getAuthToken(context: Context) {
        getInterface(context)
            .getAuthorizationToken()
            .subscribe({
                context.getSharedPreferences(CURRENT_SESSION, Context.MODE_PRIVATE)
                    .edit()
                    .putString(AUTH_TOKEN, it.get("access_token").asString)
                    .apply()
            }, {
                it.printStackTrace()
            })
    }
}