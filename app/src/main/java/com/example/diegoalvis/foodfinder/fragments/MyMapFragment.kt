package com.example.diegoalvis.foodfinder.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diegoalvis.foodfinder.R
import com.example.diegoalvis.foodfinder.models.Restaurant
import com.example.diegoalvis.foodfinder.utils.applyUISchedulers
import com.example.diegoalvis.foodfinder.utils.checkLocationPermissions
import com.example.diegoalvis.foodfinder.vm.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_map.*

class MyMapFragment : Fragment(), OnMapReadyCallback {

  companion object {
    const val TAG = "my_map_fragment"
    const val LOCATION_REQUEST_CODE = 101
    const val INITIAL_ZOOM = 14.0f
    fun newInstance() = MyMapFragment()
  }

  private lateinit var map: GoogleMap
  private lateinit var viewModel: SharedViewModel
  private val restaurantMarkerList = mutableListOf<Restaurant>()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

    val v = inflater.inflate(R.layout.fragment_map, container, false)
    viewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
    return v
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (mapView != null) {
      mapView.onCreate(savedInstanceState)
      mapView.getMapAsync(this)
    }

    setSearchEvent()

    viewModel.newSearchObserver.observeForever {
      viewModel.restaurants.value?.let { items ->
        restaurantMarkerList.clear()
        items.forEach {
          val latLng = it.coordinates.split(",")
          val latitude = latLng[0].toDouble()
          val longitude = latLng[1].toDouble()
          it.point = LatLng(latitude, longitude)
          restaurantMarkerList.add(it)
        }
        mapView.postDelayed({ addRestaurantMarkers(restaurantMarkerList) }, 500)
      }
    }
  }

  private fun setSearchEvent() {
    buttonSearch.setOnClickListener { buttonSearch ->
      map.cameraPosition.target.let { position ->
        progress.visibility = View.VISIBLE
        viewModel
          .searchRestaurants("${position.latitude},${position.longitude}", newSearch = true)
          .applyUISchedulers()
          .subscribe({
            buttonSearch.visibility = View.GONE
            progress.visibility = View.GONE
          }, {
            it.printStackTrace()
            progress.visibility = View.GONE
            activity?.findViewById<View>(android.R.id.content)?.let { Snackbar.make(it, "Connection error", Snackbar.LENGTH_LONG).show()
            }
          })
      }
    }
  }


  private fun addRestaurantMarkers(restaurantMarkerList: MutableList<Restaurant>) {
    map.apply {
      clear()
      restaurantMarkerList.forEach {
        addMarker(MarkerOptions().apply {
          position(it.point)
          icon(BitmapDescriptorFactory.fromResource(if (it.opened == 1) R.drawable.ic_location_on else R.drawable.ic_location_off))
          title(it.name)
        })
      }
    }
  }

  override fun onMapReady(googleMap: GoogleMap?) {
    googleMap?.let {
      map = it
      map.uiSettings.isZoomControlsEnabled = true
      map.uiSettings.isMyLocationButtonEnabled = true
      map.setOnCameraMoveListener { buttonSearch.visibility = View.VISIBLE }
      validatePermissions()
    }
  }

  @SuppressLint("MissingPermission")
  private fun goToMyLocation(): Boolean {
    context?.let {
      val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
      val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
      map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), INITIAL_ZOOM))
    }
    return true
  }

  @SuppressLint("MissingPermission")
  private fun validatePermissions() {
    context?.checkLocationPermissions()?.let { permissionGranted ->
      if (permissionGranted) {
        map.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener { goToMyLocation() }
        goToMyLocation()
      } else {
        requestLocationPermission()
      }
    }
  }

  private fun requestLocationPermission() {
    ActivityCompat.requestPermissions(
      activity as Activity,
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
      LOCATION_REQUEST_CODE
    )
  }

  // region lifecycle methods
  override fun onResume() {
    super.onResume()
    mapView?.run { onResume() }
  }

  override fun onPause() {
    super.onPause()
    mapView?.run { onPause() }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView?.run { onSaveInstanceState(outState) }
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView?.run { onLowMemory() }
  }
  // endregion
}


