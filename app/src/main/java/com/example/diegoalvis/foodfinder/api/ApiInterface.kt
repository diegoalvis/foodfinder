package com.diegoalvis.android.newsapp.api

import com.example.diegoalvis.foodfinder.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    /**
     * Returns a list of restaurants near a point within a country
     *
     * @param point in format lat,lng. e.j  -34.88503,-56.16561
     * @param country where the search will be done (Use 1 for Uruguay)
     */
    @GET("search/restaurants")
    fun searchRestaurants(@Query("point") point: String, @Query("country") country: Int = 1)


    /**
     * Gets the authorization token to perform request to the server
     */
    @GET("tokens")
    fun getAuthorizationToken(@Query("clientId") clientId: String = BuildConfig.CLIENT_ID,
                              @Query("clientSecret") clientSecret: String = BuildConfig.CLIENT_SECRET)
}
