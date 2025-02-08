package com.github.hummel.union.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

val gson: Gson = GsonBuilder().setPrettyPrinting().create()

const val version: Int = 3