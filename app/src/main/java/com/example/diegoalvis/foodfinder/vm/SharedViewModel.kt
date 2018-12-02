package com.example.diegoalvis.foodfinder.vm

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diegoalvis.android.newsapp.api.ApiClient
import com.example.diegoalvis.foodfinder.api.SearchRestaurantResponse
import com.example.diegoalvis.foodfinder.models.Restaurant
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

open class SharedViewModel : ViewModel() {

  private var pageRepoCounter = 1
  private var lastKeyWordSearched: String = ""
  private var lastSort: String? = null
  private var lastOrder: String? = null

  var selected = MutableLiveData<Restaurant>()
  var apiInterface = ApiClient.getInterface()
  var isLoading: ObservableBoolean = ObservableBoolean(false)
  var restaurants = MutableLiveData<List<Restaurant>>()


  fun select(item: Restaurant) {
    selected.value = item
  }

  // fetch restaurants
  fun searchRestaurants(keyWord: String, sort: String? = null, order: String? = null, page: Int = 1): Flowable<SearchRestaurantResponse> {
    lastKeyWordSearched = keyWord
    lastSort = sort
    lastOrder = order
    if (page == 1) {
      pageRepoCounter = 1
    }

    isLoading.set(true)
    return apiInterface
      .searchRestaurants(LatLng(12.0, 123.0).toString())
      .throttleFirst(1, TimeUnit.SECONDS)
      .doOnError { isLoading.set(false) }
      .doOnComplete { isLoading.set(false) }
  }

  fun getMoreRepos(): Flowable<SearchRestaurantResponse> {
    pageRepoCounter += 1
    return searchRestaurants(lastKeyWordSearched, lastSort, lastOrder, pageRepoCounter)
  }
}