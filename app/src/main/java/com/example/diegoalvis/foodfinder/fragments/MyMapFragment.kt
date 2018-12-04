package com.example.diegoalvis.foodfinder.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diegoalvis.foodfinder.R
import com.example.diegoalvis.foodfinder.models.Restaurant
import com.example.diegoalvis.foodfinder.vm.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*

class MyMapFragment : Fragment(), OnMapReadyCallback {

  companion object {
    const val TAG = "my_map_fragment"
    const val LOCATION_REQUEST_CODE = 101
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

  private val mLocationListener = object : LocationListener {
    override fun onLocationChanged(location: Location?) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (mapView != null) {
      mapView.onCreate(savedInstanceState)
      mapView.getMapAsync(this)
    }

    setSearchEvent()

    viewModel.restaurants.observe(this, Observer { items ->
      restaurantMarkerList.clear()
      items.forEach {
        val latLng = it.coordinates.split(",")
        val latitude = latLng[0].toDouble()
        val longitude = latLng[1].toDouble()
        it.point = LatLng(latitude, longitude)
        restaurantMarkerList.add(it)
      }
      mapView.postDelayed({ addRestaurantMarkers(restaurantMarkerList) }, 500)
    })
  }

  private fun setSearchEvent() {
    buttonSearch.setOnClickListener { buttonSearch ->
      map.cameraPosition.target.let { position ->
        progress.visibility = View.VISIBLE
        viewModel
          .searchRestaurants("${position.latitude},${position.longitude}", newSearch = true)
          .subscribe({
            buttonSearch.visibility = View.GONE
            progress.visibility = View.GONE
          }, {
            it.printStackTrace()
            progress.visibility = View.GONE
          })
      }
    }
  }


  private fun addRestaurantMarkers(restaurantMarkerList: MutableList<Restaurant>) {
    map.apply {
      clear()
      restaurantMarkerList.forEach {
        addMarker(
          MarkerOptions().apply {
            position(it.point)
            icon(BitmapDescriptorFactory.fromResource(if (it.opened == 1) R.drawable.ic_location_on else R.drawable.ic_location_off))
            title(it.name)
          }
        )
      }
    }
  }


  override fun onMapReady(googleMap: GoogleMap?) {
    googleMap?.let {
      map = it
      map.uiSettings.isZoomControlsEnabled = true
      map.uiSettings.isMyLocationButtonEnabled = true
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.88503, -56.16561), 14.0f)) // Montevideo
      map.setOnCameraMoveListener { buttonSearch.visibility = View.VISIBLE }
      validatePermissions()
    }
  }

  private fun goToMyLocation(): Boolean {
    if (map.myLocation != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(map.myLocation.latitude, map.myLocation.longitude), 14.0f))
    }
    return true
  }


  private fun validatePermissions() {
    context?.let {
      if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        requestLocationPermission()
      } else {
        map.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener { goToMyLocation() }
      }
    }
  }

  private fun requestLocationPermission() {
//    ActivityCompat.requestPermissions(this@MyMapFragment.activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)
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


