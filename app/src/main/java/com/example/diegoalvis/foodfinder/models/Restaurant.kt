package com.example.diegoalvis.foodfinder.models

data class Restaurant(
    val deliveryTimeMinMinutes: String,
    val cityName: String,
    val allCategories: String,
    val sortingDistance: Float,
    val minDeliveryAmount: Int,
    val description: String,
    val opened: Int, // 1 opened, 0 closed
    val isNew: Boolean,
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
    val paymentMethods: String
)