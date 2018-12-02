package com.example.diegoalvis.foodfinder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.diegoalvis.android.newsapp.api.ApiClient
import com.example.diegoalvis.foodfinder.fragments.MyMapFragment
import com.example.diegoalvis.foodfinder.fragments.RestaurantListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private val mapFragment = MyMapFragment.newInstance()
  private val listFragment = RestaurantListFragment.newInstance()

  private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    var fragment: Fragment? = null
    when (item.itemId) {
      R.id.navigation_home -> {
        fragment = listFragment
      }
      R.id.navigation_dashboard -> {
        fragment = mapFragment
      }
      R.id.navigation_notifications -> {

      }
    }

    fragment?.let {
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.container, it, MyMapFragment.TAG)
        .commit()
        return@OnNavigationItemSelectedListener true
    }

    false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
  }
}
