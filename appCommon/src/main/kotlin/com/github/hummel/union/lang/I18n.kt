package com.github.hummel.union.lang

import com.github.hummel.union.bean.ServerData
import com.google.gson.Gson

object I18n {
	@Suppress("UNCHECKED_CAST")
	fun of(codename: String, serverData: ServerData): String {
		val instance = when (serverData.lang) {
			"ru" -> LangRuRu
			"be" -> LangBeBy
			"en" -> LangEnUs
			else -> throw Exception()
		}

		val gson = Gson()
		val translation = gson.fromJson(instance.getJson(), Map::class.java) as Map<String, String>

		translation[codename]?.let {
			return@of it
		} ?: run {
			return@of "Invalid translation key!"
		}
	}
}