package com.example.diegoalvis.foodfinder.vm

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diegoalvis.android.newsapp.api.ApiClient
import com.example.diegoalvis.foodfinder.api.SearchRestaurantResponse
import com.example.diegoalvis.foodfinder.models.Restaurant
import com.example.diegoalvis.foodfinder.utils.applyUISchedulers
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

open class SharedViewModel : ViewModel() {

  private val NUM_OF_RESULTS = 20

  private var offset = 0
  private var lastLatLngSearched: String = ""
  private var lastSort: String? = null

  val selected = MutableLiveData<Restaurant>()
  val apiInterface = ApiClient.getInterface()
  val isLoading: ObservableBoolean = ObservableBoolean(false)
  val restaurants = MutableLiveData<MutableList<Restaurant>>()
  val newSearchObserver = MutableLiveData<Boolean>()

  fun select(item: Restaurant) {
    selected.value = item
  }

  // fetch restaurants
  fun searchRestaurants(latLng: String, sort: String? = null, newSearch: Boolean = false): Flowable<SearchRestaurantResponse> {
    lastLatLngSearched = latLng
    lastSort = sort
    if (newSearch) {
      offset = 0
    }

    isLoading.set(true)
    return apiInterface
      .searchRestaurants(latLng, offset = offset, max = NUM_OF_RESULTS)
      .throttleFirst(1, TimeUnit.SECONDS)
      .applyUISchedulers()
      .doOnNext { response ->
        if (newSearch) {
          restaurants.value = response.data
        } else {
          restaurants.value?.addAll(response.data)
        }
        newSearchObserver.value = newSearch
      }
      .doOnError { isLoading.set(false) }
      .doOnComplete { isLoading.set(false) }
  }

  fun getMoreRepos(): Flowable<SearchRestaurantResponse> {
    offset += NUM_OF_RESULTS
    return searchRestaurants(lastLatLngSearched, lastSort)
  }
}