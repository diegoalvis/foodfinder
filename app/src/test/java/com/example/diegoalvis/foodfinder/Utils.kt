package com.example.diegoalvis.foodfinder

import com.example.diegoalvis.foodfinder.api.SearchRestaurantResponse
import com.example.diegoalvis.foodfinder.models.Restaurant
import com.google.android.gms.maps.model.LatLng
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

// Test
inline fun <reified T> mock() = Mockito.mock(T::class.java)

inline fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)


// Testing classes
val searchResponseTest = SearchRestaurantResponse(0, 0, "", 0, mutableListOf())

val restaurantTest = Restaurant(
  "45",
  "Montevideo",
  "Bebidas,Comida Árabe,Hamburguesas,Menú del día,Pizzas,Postres,Lehmeyun",
  0.022f,
  200,
  "Los tiempos de demora son estimados, el proveedor confirmará el tiempo real estimado en que recibirá el pedido.",
  1,
  false,
  "-34.9032,-56.1532",
  "-",
  "2010-01-01T00:00:00Z",
  true,
  0,
  "Entre 45' y 60'",
  2.4f,
  "Mr. Kebap's",
  4.67f,
  "mr-kebap.jpg",
  "Rivera",
  "Efectivo",
  point = LatLng(0.0, 0.0)
)

