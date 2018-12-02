package com.example.diegoalvis.foodfinder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.diegoalvis.android.newsapp.api.ApiClient
import com.example.diegoalvis.foodfinder.fragments.MyMapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  val mapFragment = MyMapFragment.newInstance()

  private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    when (item.itemId) {
      R.id.navigation_home -> {
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, mapFragment, MyMapFragment.TAG)
          .commit()
        container
        return@OnNavigationItemSelectedListener true
      }
      R.id.navigation_dashboard -> {
        return@OnNavigationItemSelectedListener true
      }
      R.id.navigation_notifications -> {
        return@OnNavigationItemSelectedListener true
      }
    }
    false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
  }

  override fun onResume() {
    super.onResume()
    val service = ApiClient.getInterface(this)

    service.searchRestaurants("-34.88503,-56.16561")
      .subscribe({
        Log.e("ALVis", " Success request")
      }, {
        it.printStackTrace()
      })

  }
}
