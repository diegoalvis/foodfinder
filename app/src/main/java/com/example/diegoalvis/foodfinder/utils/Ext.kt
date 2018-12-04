package com.example.diegoalvis.foodfinder.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// ViewGroup
fun ViewGroup.inflate(layout:Int) = LayoutInflater.from(context).inflate(layout, this, false)

// Fragment
fun FragmentManager.replace(container: Int, fragment: Fragment, tag: String) { this.beginTransaction().replace(container, fragment, tag).commitAllowingStateLoss() }

fun FragmentManager.add(container: Int, fragment: Fragment, tag: String) { this.beginTransaction().add(container, fragment, tag).commitAllowingStateLoss() }

fun FragmentManager.show(fragment: Fragment) {
  this.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).show(fragment).commitAllowingStateLoss()
}

fun FragmentManager.hide(fragment: Fragment) { this.beginTransaction().hide(fragment).commitAllowingStateLoss() }

// Context
fun Context.checkLocationPermissions()
  = (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

// RX
fun <T> Flowable<T>.applyUISchedulers()
        = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.applyTestSchedulers()
        = this.subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline())

fun <T> Observable<T>.applyUISchedulers()
        = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.applyTestSchedulers()
        = this.subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline())

