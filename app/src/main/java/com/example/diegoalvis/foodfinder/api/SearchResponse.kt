package com.diegoalvis.android.newsapp.api

import com.example.diegoalvis.watchersexplorer.models.Repo
import com.google.gson.annotations.SerializedName

class SearchResponse(var totalCount: Int, val items: MutableList<Repo> = mutableListOf())