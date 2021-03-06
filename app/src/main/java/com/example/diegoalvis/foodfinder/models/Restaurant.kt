package com.example.diegoalvis.foodfinder.models

import com.google.android.gms.maps.model.LatLng

data class Restaurant(
    val deliveryTimeMinMinutes: String,
    val cityName: String,
    val allCategories: String,
    val sortingDistance: Float,
    val minDeliveryAmount: Int,
    val description: String,
    val opened: Int, // 1 opened, 0 closed
    val isNew: Boolean,
    val coordinates: String,
    val businessType: String,
    val nextHourClose: String,
    val hasOnlinePaymentMethods: Boolean,
    val discount: Int,
    val deliveryTime: String,
    val distance: Float,
    val name: String,
    val ratingScore: Float,
    val logo: String,
    val address: String,
    val paymentMethods: String,
    var point: LatLng
)