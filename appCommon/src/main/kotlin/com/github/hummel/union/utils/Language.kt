package com.github.hummel.union.utils

import com.github.hummel.union.bean.ServerData
import com.github.hummel.union.lang.LangBeBy
import com.github.hummel.union.lang.LangEnUs
import com.github.hummel.union.lang.LangRuRu
import com.google.gson.Gson
import java.time.Month

enum class Lang {
	NO_CONNECTION, EXIT, NUKE, RANDOM, NO_BIRTHDAYS, HAPPY_BIRTHDAY, NO_ACCESS, IMPORT, INVALID_ARG, INVALID_FORMAT, SET_LANGUAGE, CURRENT_CHANCE, CURRENT_LANG, SET_CHANCE, ADDED_MANAGER, ADDED_CHANNEL, ADDED_BIRTHDAY, REMOVED_BIRTHDAY, REMOVED_MANAGER, REMOVED_CHANNEL, CLEARED_CHANNELS, CLEARED_MANAGERS, CLEARED_DATA, CLEARED_MESSAGES, CLEARED_BIRTHDAYS, GAME_YES_1, GAME_YES_2, GAME_YES_3, GAME_YES_4, GAME_NO_1, GAME_NO_2, GAME_NO_3, GAME_NO_4, MSG_ACCESS, MSG_ERROR, MSG_SUCCESS;

	@Suppress("UNCHECKED_CAST")
	operator fun get(serverData: ServerData): String {
		val json = when (serverData.lang) {
			"ru" -> LangRuRu.JSON
			"be" -> LangBeBy.JSON
			"en" -> LangEnUs.JSON
			else -> throw Exception()
		}

		val gson = Gson()
		val langMap = gson.fromJson(json, Map::class.java) as Map<String, String>
		val key = name
		langMap[key]?.let {
			return@get it
		} ?: run {
			throw Exception()
		}
	}
}

fun getFormattedTranslatedDate(month: Month, serverData: ServerData, day: Int): String {
	return when (month) {
		Month.JANUARY -> if (serverData.lang == "ru") "$day января" else "January $day"
		Month.FEBRUARY -> if (serverData.lang == "ru") "$day февраля" else "February $day"
		Month.MARCH -> if (serverData.lang == "ru") "$day марта" else "March $day"
		Month.APRIL -> if (serverData.lang == "ru") "$day апреля" else "April $day"
		Month.MAY -> if (serverData.lang == "ru") "$day мая" else "May $day"
		Month.JUNE -> if (serverData.lang == "ru") "$day июня" else "June $day"
		Month.JULY -> if (serverData.lang == "ru") "$day июля" else "July $day"
		Month.AUGUST -> if (serverData.lang == "ru") "$day августа" else "August $day"
		Month.SEPTEMBER -> if (serverData.lang == "ru") "$day сентября" else "September $day"
		Month.OCTOBER -> if (serverData.lang == "ru") "$day октября" else "October $day"
		Month.NOVEMBER -> if (serverData.lang == "ru") "$day ноября" else "November $day"
		Month.DECEMBER -> if (serverData.lang == "ru") "$day декабря" else "December $day"
	}
}