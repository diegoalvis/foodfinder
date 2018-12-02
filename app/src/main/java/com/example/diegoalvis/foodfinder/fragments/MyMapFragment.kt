package com.example.diegoalvis.foodfinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.diegoalvis.foodfinder.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_map.*


class MyMapFragment : Fragment(), OnMapReadyCallback {

  companion object {
    const val TAG = "my_map_fragment"
    fun newInstance() = MyMapFragment()
  }

  private lateinit var map: GoogleMap

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

    val v = inflater.inflate(R.layout.fragment_map, container, false)
    return v
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    MapsInitializer.initialize(context)
    if (mapView != null) {
      mapView.onCreate(savedInstanceState)
      mapView.getMapAsync(this)
    }
  }


  override fun onMapReady(googleMap: GoogleMap?) {
    googleMap?.run {
      map = this
      map.uiSettings.isZoomControlsEnabled = true
//            map.addMarker(MarkerOptions().position(LatLng())
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(/*some location*/, 10))
    }
  }


  // region lifecycle methods
  override fun onResume() {
    super.onResume()
    mapView?.run { onPause() }
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
