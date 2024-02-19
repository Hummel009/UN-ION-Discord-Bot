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
	return when (serverData.lang) {
		"ru" -> {
			when (month) {
				Month.JANUARY -> "$day января"
				Month.FEBRUARY -> "$day февраля"
				Month.MARCH -> "$day марта"
				Month.APRIL -> "$day апреля"
				Month.MAY -> "$day мая"
				Month.JUNE -> "$day июня"
				Month.JULY -> "$day июля"
				Month.AUGUST -> "$day августа"
				Month.SEPTEMBER -> "$day сентября"
				Month.OCTOBER -> "$day октября"
				Month.NOVEMBER -> "$day ноября"
				Month.DECEMBER -> "$day декабря"
			}
		}

		"be" -> {
			when (month) {
				Month.JANUARY -> "$day студзеня"
				Month.FEBRUARY -> "$day лютага"
				Month.MARCH -> "$day сакавіка"
				Month.APRIL -> "$day красавіка"
				Month.MAY -> "$day траўня"
				Month.JUNE -> "$day чэрвеня"
				Month.JULY -> "$day ліпеня"
				Month.AUGUST -> "$day жніўня"
				Month.SEPTEMBER -> "$day верасня"
				Month.OCTOBER -> "$day кастрычніка"
				Month.NOVEMBER -> "$day лістапада"
				Month.DECEMBER -> "$day снежня"
			}
		}

		"en" -> {
			when (month) {
				Month.JANUARY -> "January $day"
				Month.FEBRUARY -> "February $day"
				Month.MARCH -> "March $day"
				Month.APRIL -> "April $day"
				Month.MAY -> "May $day"
				Month.JUNE -> "June $day"
				Month.JULY -> "July $day"
				Month.AUGUST -> "August $day"
				Month.SEPTEMBER -> "September $day"
				Month.OCTOBER -> "October $day"
				Month.NOVEMBER -> "November $day"
				Month.DECEMBER -> "December $day"
			}
		}

		else -> throw Exception()
	}

}