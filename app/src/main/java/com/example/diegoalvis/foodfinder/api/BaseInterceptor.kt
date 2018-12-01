package com.example.diegoalvis.foodfinder.api

import android.content.Context
import androidx.annotation.NonNull
import com.diegoalvis.android.newsapp.api.ApiClient
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

private const val CURRENT_SESSION = "current_session"
private const val AUTH_TOKEN = "auth_token"

class BaseHeaderInterceptor(private val context: Context) : Interceptor {

    val service = ApiClient.getInterface(context)

    @Throws(IOException::class)
    override fun intercept(@NonNull chain: Interceptor.Chain): Response {
        val original = chain.request()
        val authToken = context.getSharedPreferences(CURRENT_SESSION, Context.MODE_PRIVATE).getString(AUTH_TOKEN, null)

        val response = authToken?.let {
            val request = original.newBuilder().header("Authorization", authToken)
            chain.proceed(request.build())
        } ?: chain.proceed(original)

        // try the request
        if (response?.code() == 403) {
            // get a new token (I use a synchronous Retrofit call)
//            service.getWatchers()

            // retry the request
//            return chain.proceed(newRequest);
        }

        // otherwise just pass the original response on
        return response
    }
}
