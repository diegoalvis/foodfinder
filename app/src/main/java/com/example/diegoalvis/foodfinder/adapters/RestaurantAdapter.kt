package com.example.diegoalvis.foodfinder.adapters

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.diegoalvis.foodfinder.R
import com.example.diegoalvis.foodfinder.databinding.ItemRestaurantBinding
import com.example.diegoalvis.foodfinder.models.Restaurant
import com.example.diegoalvis.foodfinder.utils.inflate
import com.squareup.picasso.Picasso
import java.util.*

class RestaurantAdapter(private val callback: (pos: Int) -> Unit) : RecyclerView.Adapter<RepoViewHolder>() {

  var data = mutableListOf<Restaurant>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
    holder.binding?.place = data[position]
    holder.binding?.root?.tag = position
    holder.binding?.handler = this

    // Load restaurant logo
//    val listImages = listOf(R.drawable.fallback_burguer, R.drawable.fallback_pizza, R.drawable.fallback_tacos)
//    Picasso.get().load(listImages[Random().nextInt(101).rem(3)]).into(holder.binding?.logo)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    RepoViewHolder(parent.inflate(R.layout.item_restaurant))

  override fun getItemCount(): Int = data.size

  fun onRepoClick(pos: Int) {
    callback(pos)
  }
}

class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  val binding: ItemRestaurantBinding? = DataBindingUtil.bind(view)
}