package com.diegoalvis.android.newsapp.api

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val BASE_URL: String = "https://api.github.com"
    private var retrofit: Retrofit? = null

    @Synchronized
    private fun buildRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

    fun getInterface(): ApiInterface {
        if (retrofit == null) {
            retrofit = buildRetrofit()
        }
        return retrofit!!.create(ApiInterface::class.java)
    }
}