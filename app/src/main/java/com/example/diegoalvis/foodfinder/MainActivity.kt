package com.example.diegoalvis.foodfinder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.diegoalvis.android.newsapp.api.ApiClient
import com.example.diegoalvis.foodfinder.fragments.MyMapFragment
import com.example.diegoalvis.foodfinder.fragments.RestaurantListFragment
import com.example.diegoalvis.foodfinder.utils.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_restaurant_fragment.*

class MainActivity : AppCompatActivity() {

  private val mapFragment = MyMapFragment.newInstance()
  private val listFragment = RestaurantListFragment.newInstance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
  }

  override fun onResume() {
    super.onResume()
    supportFragmentManager.replace(R.id.container, listFragment, RestaurantListFragment.TAG)
  }

  private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    var fragment: Fragment? = null
    var tag = ""
    when (item.itemId) {
      R.id.navigation_home -> {
        fragment = listFragment
        tag = RestaurantListFragment.TAG
      }
      R.id.navigation_dashboard -> {
        fragment = mapFragment
        tag = MyMapFragment.TAG
      }
      R.id.navigation_notifications -> {

      }
    }

    fragment?.let {
      supportFragmentManager.replace(R.id.container, it, tag)
      return@OnNavigationItemSelectedListener true
    }

    false
  }

}
