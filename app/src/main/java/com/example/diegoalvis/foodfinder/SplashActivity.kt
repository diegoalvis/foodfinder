package com.example.diegoalvis.foodfinder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

  companion object {
    const val LOCATION_REQUEST_CODE = 101
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    setupPermissions()
  }

  private fun setupPermissions() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
      || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      requestLocationPermission()
    } else {
      startMainActivity()
    }
  }

  private fun requestLocationPermission() {
    ActivityCompat.requestPermissions(
      this,
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
      LOCATION_REQUEST_CODE
    )
  }


  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    when (requestCode) { LOCATION_REQUEST_CODE ->
      if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        //Permission has been denied by user
        requestLocationPermission()
      } else {
        startMainActivity()
      }
    }
  }

  private fun startMainActivity() {
    author.postDelayed({startActivity<MainActivity>()}, 500)
  }
}
