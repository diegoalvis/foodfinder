package com.example.diegoalvis.foodfinder.api

import com.example.diegoalvis.foodfinder.models.Restaurant

class SearchRestaurantResponse(val total: Int,
                               val max: Int,
                               val sort: String,
                               val count: Int,
                               val data: List<Restaurant> = listOf())