package com.github.hummel.union.lang

object LangRuRu : Lang {
	override fun getJson(): String = """
		{
			"NO_CONNECTION": "Сайт с нейросетью временно недоступен.",
			"EXIT": "Приложение будет выключено.",
			"NUKE": "Сообщения удалены.",
			"RANDOM": "Случайное число: %d.",
			"NO_BIRTHDAYS": "Дней рождения нет.",
			"HAPPY_BIRTHDAY": "<@%d>, с днём рождения!",
			"NO_ACCESS": "Вы не можете использовать эту команду.",
			"IMPORT": "Данные импортированы.",
			"INVALID_ARG": "Недопустимые аргументы.",
			"INVALID_FORMAT": "Недопустимый формат аргументов.",
			"SET_LANGUAGE": "Установлен язык: %s.",
			"SET_CHANCE": "Установлен шанс сообщения: %d.",
			"CURRENT_LANGUAGE": "Текущий язык интерфейса: %s.",
			"CURRENT_CHANCE": "Текущий шанс сообщения: %d.",
			"ADDED_MANAGER": "Добавлена управляющая роль: <@&%d>.",
			"ADDED_CHANNEL": "Добавлен секретный канал: <#%d>.",
			"ADDED_BIRTHDAY": "Добавлен день рождения: <@%d>, %s.",
			"REMOVED_MANAGER": "Удалена управляющая роль: <@&%d>.",
			"REMOVED_CHANNEL": "Удалён секретный канал: <#%d>.",
			"REMOVED_BIRTHDAY": "Удалён день рождения: <@%d>.",
			"CLEARED_MANAGERS": "Управляющие роли сервера очищены.",
			"CLEARED_CHANNELS": "Секретные каналы сервера очищены.",
			"CLEARED_BIRTHDAYS": "Дни рождения сервера очищены.",
			"CLEARED_DATA": "Данные сервера очищены.",
			"CLEARED_MESSAGES": "Сообщения сервера очищены.",
			"GAME_YES_1": "Да.",
			"GAME_YES_2": "Можешь не сомневаться в этом)",
			"GAME_YES_3": "Так и есть!",
			"GAME_YES_4": "Я уверен в этом на все сто процентов!",
			"GAME_NO_1": "Нет.",
			"GAME_NO_2": "Я считаю, что нет)",
			"GAME_NO_3": "Однозначно нет!",
			"GAME_NO_4": "Нет, я уверен в этом на все сто процентов.",
			"MSG_ACCESS": "Доступ",
			"MSG_ERROR": "Ошибка",
			"MSG_SUCCESS": "Успех",
			"JANUARY": "%d января",
			"FEBRUARY": "%d февраля",
			"MARCH": "%d марта",
			"APRIL": "%d апреля",
			"MAY": "%d мая",
			"JUNE": "%d июня",
			"JULY": "%d июля",
			"AUGUST": "%d августа",
			"SEPTEMBER": "%d сентября",
			"OCTOBER": "%d октября",
			"NOVEMBER": "%d ноября",
			"DECEMBER": "%d декабря",
			"birthday": "<@%d>: %s"
		}
	""".trimIndent()
}