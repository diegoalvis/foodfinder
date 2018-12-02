package com.diegoalvis.android.newsapp.api

import android.annotation.SuppressLint
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {

  private val BASE_URL: String = "http://stg-api.pedidosya.com/public/v1/"
  private var retrofit: Retrofit? = null

  var authToken = ""

  fun getInterface(): ApiInterface {
    if (retrofit == null) {
      retrofit = buildRetrofit()
    }
    return retrofit!!.create(ApiInterface::class.java)
  }

  @Synchronized
  private fun buildRetrofit() = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    .client(getOkHttpClientTokenInstance())
    .build()

  private fun getOkHttpClientTokenInstance() =
    OkHttpClient.Builder()
      .readTimeout(10, TimeUnit.SECONDS)
      .connectTimeout(10, TimeUnit.SECONDS)
      .addInterceptor(headersInterceptor())
      .build()


  private fun headersInterceptor() = Interceptor { chain ->
    val original = chain.request()
    val request = original.newBuilder()
      .header("Authorization", authToken)
      .header("Content-Type", "application/json; charset=utf-8")

    val response = chain.proceed(request.build())
    if (authToken.isEmpty() || response.code() == 403) {
      getAuthToken()
    }
    response
  }

  @SuppressLint("CheckResult")
  fun getAuthToken() {
    getInterface()
      .getAuthorizationToken()
      .subscribe({
        authToken = it.get("access_token").asString
      }, {
        it.printStackTrace()
      })
  }
}