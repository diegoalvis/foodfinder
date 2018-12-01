package com.diegoalvis.android.newsapp.api

import android.content.Context
import com.example.diegoalvis.foodfinder.api.BaseHeaderInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

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
            .addInterceptor(BaseHeaderInterceptor(context)).build()

}