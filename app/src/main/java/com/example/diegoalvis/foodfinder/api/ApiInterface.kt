package com.diegoalvis.android.newsapp.api

import com.example.diegoalvis.foodfinder.BuildConfig
import com.example.diegoalvis.foodfinder.api.SearchRestaurantResponse
import com.google.gson.JsonObject
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {

    /**
     * Returns a list of restaurants near a point within a country
     *
     * @param point in format lat,lng. e.j  -34.88503,-56.16561
     * @param country where the search will be done (Use 1 for Uruguay)
     */
    @GET("search/restaurants")
    fun searchRestaurants(@Query("point") point: String, @Query("country") country: Int = 1): Flowable<SearchRestaurantResponse>


    /**
     * Gets the authorization token to perform request to the server
     */
    @GET("tokens")
    fun getAuthorizationToken(@Query("clientId") clientId: String = "trivia_f",
                              @Query("clientSecret") clientSecret: String = "PeY@@Tr1v1@943"): Flowable<JsonObject>
}
