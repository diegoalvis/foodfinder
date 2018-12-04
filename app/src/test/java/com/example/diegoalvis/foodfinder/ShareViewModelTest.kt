package com.example.diegoalvis.foodfinder

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.diegoalvis.android.newsapp.api.ApiInterface
import com.example.diegoalvis.foodfinder.models.Restaurant
import com.example.diegoalvis.foodfinder.utils.applyTestSchedulers
import com.example.diegoalvis.foodfinder.vm.SharedViewModel
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.verify

class ShareViewModelTest {

  val apiInterface = mock<ApiInterface>()
  val observableBoolean = mock<ObservableBoolean>()
  val sharedViewModel = mock<SharedViewModel>()
  val restaurantData = mock<MutableLiveData<Restaurant>>()

  companion object {
    const val POINT_TEST = "0.0,0.0"
  }

  @Before
  fun setUp() {
    sharedViewModel.isLoading = observableBoolean
    sharedViewModel.apiInterface = apiInterface
  }

  @Test
  fun searchRestaurants() {
    val searchResponse = Flowable.just(searchResponseTest)
    given(apiInterface.searchRestaurants(POINT_TEST)).willReturn(searchResponse)

    whenever(sharedViewModel.searchRestaurants(POINT_TEST).applyTestSchedulers()).thenReturn(searchResponse)
  }

  @Test
  fun loadMoreRestaurants() {
    sharedViewModel.lastLatLngSearched = POINT_TEST

    val searchResponse = Flowable.just(searchResponseTest)
    given(apiInterface.searchRestaurants(POINT_TEST, offset = SharedViewModel.NUM_OF_RESULTS)).willReturn(searchResponse)

    whenever(sharedViewModel.getMoreRestaurants().applyTestSchedulers()).thenReturn(searchResponse)
  }

  @Test
  fun verifyLoaderVisibile() {
    val searchResponse = Flowable.just(searchResponseTest)
    given(restaurantData.value).willReturn(restaurantTest)
    given(apiInterface.searchRestaurants(POINT_TEST)).willReturn(searchResponse)

    sharedViewModel.selected = restaurantData
    sharedViewModel.searchRestaurants(POINT_TEST).applyTestSchedulers()

    // verify loader visibility
    verify(sharedViewModel.isLoading, Mockito.times(1)).set(true)
  }
}
