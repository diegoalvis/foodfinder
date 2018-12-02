package com.example.diegoalvis

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.diegoalvis.android.newsapp.api.ApiClient

class Application : Application() {

  private val CURRENT_SESSION = "current_session"
  private val AUTH_TOKEN = "auth_token"

  @SuppressLint("CheckResult")
  override fun onCreate() {
    super.onCreate()
    ApiClient.getInterface()
      .getAuthorizationToken()
      .subscribe({
        val authToken = it.get("access_token").asString
        ApiClient.authToken = authToken
        getSharedPreferences(CURRENT_SESSION, Context.MODE_PRIVATE)
          .edit()
          .putString(AUTH_TOKEN, authToken)
          .apply()
      }, { throwable ->
        throwable.printStackTrace()
        getSharedPreferences(CURRENT_SESSION, Context.MODE_PRIVATE).getString(AUTH_TOKEN, "")?.let {
          ApiClient.authToken = it
        }
      })
  }

}