package com.example.diegoalvis.foodfinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diegoalvis.foodfinder.R
import com.example.diegoalvis.foodfinder.vm.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*


class MyMapFragment : Fragment(), OnMapReadyCallback {

  companion object {
    const val TAG = "my_map_fragment"
    fun newInstance() = MyMapFragment()
  }

  private lateinit var map: GoogleMap
  private lateinit var viewModel: SharedViewModel
  private val restaurantMarkerList = mutableListOf<LatLng>()

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

    viewModel.restaurants.observe(this, Observer { items ->
      items.distinct().forEach {
        val latLng = it.coordinates.split(",")
        val latitude = latLng[0].toDouble()
        val longitude = latLng[1].toDouble()
        map.run {
          restaurantMarkerList.add(LatLng(latitude, longitude))
        }
      }
    })
  }


  override fun onMapReady(googleMap: GoogleMap?) {
    googleMap?.run {
      map = this
      map.uiSettings.isZoomControlsEnabled = true
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.88503, -56.16561), 10.0f))
      map.addMarker(MarkerOptions().position(LatLng(-34.88503, -56.16561)))
//      restaurantMarkerList.forEach { map.addMarker(MarkerOptions().position(it)) }
    }
  }


  // region lifecycle methods
  override fun onResume() {
    super.onResume()
    mapView?.run { onResume() }
    restaurantMarkerList.forEach { map.addMarker(MarkerOptions().position(it)) }
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
