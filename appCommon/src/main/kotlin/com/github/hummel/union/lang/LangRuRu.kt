package com.github.hummel.union.lang

object LangRuRu : Lang {
	override fun getJson(): String = """
		{
			"no_connection": "Сайт с нейросетью временно недоступен.",
			"exit": "Приложение будет выключено.",
			"nuke": "Сообщения удалены.",
			"random": "Случайное число: %d.",
			"no_managers": "Управляющих ролей нет.",
			"has_managers": "Управляющие роли:",
			"no_birthdays": "Дней рождения нет.",
			"has_birthdays": "Дни рождения:",
			"no_secret_channels": "Секретных каналов нет.",
			"has_secret_channels": "Секретные каналы:",
			"happy_birthday": "<@%d>, с днём рождения!",
			"no_access": "Вы не можете использовать эту команду.",
			"import": "Данные импортированы.",
			"invalid_arg": "Недопустимые аргументы.",
			"invalid_format": "Недопустимый формат аргументов.",
			"set_language": "Установлен язык: %s.",
			"set_chance": "Установлен шанс сообщения: %d.",
			"current_language": "Текущий язык интерфейса: %s.",
			"current_chance": "Текущий шанс сообщения: %d.",
			"added_manager": "Добавлена управляющая роль: <@&%d>.",
			"added_channel": "Добавлен секретный канал: <#%d>.",
			"added_birthday": "Добавлен день рождения: <@%d>, %s.",
			"removed_manager": "Удалена управляющая роль: <@&%d>.",
			"removed_channel": "Удалён секретный канал: <#%d>.",
			"removed_birthday": "Удалён день рождения: <@%d>.",
			"cleared_managers": "Управляющие роли сервера очищены.",
			"cleared_channels": "Секретные каналы сервера очищены.",
			"cleared_birthdays": "Дни рождения сервера очищены.",
			"cleared_data": "Данные сервера очищены.",
			"cleared_messages": "Сообщения сервера очищены.",
			"game_yes_1": "Да.",
			"game_yes_2": "Можешь не сомневаться в этом)",
			"game_yes_3": "Так и есть!",
			"game_yes_4": "Я уверен в этом на все сто процентов!",
			"game_no_1": "Нет.",
			"game_no_2": "Я считаю, что нет)",
			"game_no_3": "Однозначно нет!",
			"game_no_4": "Нет, я уверен в этом на все сто процентов.",
			"msg_access": "Доступ",
			"msg_error": "Ошибка",
			"msg_success": "Успех",
			"january": "%d января",
			"february": "%d февраля",
			"march": "%d марта",
			"april": "%d апреля",
			"may": "%d мая",
			"june": "%d июня",
			"july": "%d июля",
			"august": "%d августа",
			"september": "%d сентября",
			"october": "%d октября",
			"november": "%d ноября",
			"december": "%d декабря",
			"user_birthday": "<@%d>: %s",
			"secret_channel": "<#%d>",
			"manager": "<@&%d>",
			"choice_set": "Мне предложили выбрать один элемент из набора: %s",
			"choice_select": "Я выбираю... %s",
			"be": "белорусский",
			"en": "английский",
			"ru": "русский"
		}
	""".trimIndent()
}