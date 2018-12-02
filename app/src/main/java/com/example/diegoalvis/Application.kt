package com.example.diegoalvis

import android.app.Application
import com.diegoalvis.android.newsapp.api.ApiClient

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClient.getAuthToken(this)
    }

}