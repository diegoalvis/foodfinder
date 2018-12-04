package com.example.diegoalvis.foodfinder

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.diegoalvis.android.newsapp.api.ApiClient
import com.example.diegoalvis.foodfinder.fragments.MyMapFragment
import com.example.diegoalvis.foodfinder.fragments.RestaurantListFragment
import com.example.diegoalvis.foodfinder.utils.add
import com.example.diegoalvis.foodfinder.utils.applyUISchedulers
import com.example.diegoalvis.foodfinder.utils.hide
import com.example.diegoalvis.foodfinder.utils.replace
import com.example.diegoalvis.foodfinder.utils.show
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list_restaurant.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), RestaurantListFragment.OnSearchListener {

  private val mapFragment = MyMapFragment.newInstance()
  private val listFragment = RestaurantListFragment.newInstance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    listFragment.onSearchListener = this
  }

  override fun onSearch() {
    supportFragmentManager.hide(listFragment)
    supportFragmentManager.show(mapFragment)
    navigation.selectedItemId = R.id.navigation_map
  }

  override fun onResume() {
    super.onResume()
    // initial view
    supportFragmentManager.add(R.id.container, mapFragment, MyMapFragment.TAG)
    supportFragmentManager.add(R.id.container, listFragment, RestaurantListFragment.TAG)
    supportFragmentManager.hide(mapFragment)

    setAccessInternetObserver()
  }

  @SuppressLint("CheckResult")
  private fun setAccessInternetObserver() {
    val connectionState = Snackbar.make(container, "No Internet connection.", Snackbar.LENGTH_INDEFINITE).setAction("CLOSE") { }
    Observable.interval(1, 10, TimeUnit.SECONDS)
      .flatMap { ReactiveNetwork.observeInternetConnectivity() }
      .applyUISchedulers()
      .subscribe({ isConnected ->
        if (isConnected) {
          connectionState.dismiss()
        } else if (!connectionState.isShown) {
          connectionState.show()
          progress.visibility = View.GONE
        }
      }, {
        Toast.makeText(this, "Error when trying to get connection status", Toast.LENGTH_SHORT).show()
      })
  }

  private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    when (item.itemId) {
      R.id.navigation_list -> {
        supportFragmentManager.show(listFragment)
        supportFragmentManager.hide(mapFragment)
      }
      R.id.navigation_map -> {
        supportFragmentManager.hide(listFragment)
        supportFragmentManager.show(mapFragment)
      }
    }
    true
  }
}