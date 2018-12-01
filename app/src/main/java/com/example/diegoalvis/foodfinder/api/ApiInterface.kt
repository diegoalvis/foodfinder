package com.diegoalvis.android.newsapp.api

import com.example.diegoalvis.watchersexplorer.models.Owner
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("/search/repositories")
    fun searchRepos(@Query("q") keyWords: String, @Query("sort") sort: String? = null, @Query("order") order: String? = null, @Query("page") page: Int? = 1): Flowable<SearchResponse>

    @GET("/repos/{owner}/{repo}/subscribers")
    fun getWatchers(@Path("owner") owner: String, @Path("repo") repo: String, @Query("page") page: Int? = 1): Flowable<MutableList<Owner>>
}