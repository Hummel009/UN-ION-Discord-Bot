package com.github.hummel.union.lang

object LangUkUa : Lang {
	override fun getJson(): String = """
		{
			"no_connection": "Сайт з нейромережею терміново недоступний.",
			"exit": "Застосунок буде вимкнено.",
			"nuke": "Повідомлення видалені.",
			"random": "Випадкове число: %d.",
			"no_managers": "Керівницьких ролей немає.",
			"has_managers": "Керівницькі ролі:",
			"no_birthdays": "Днів народження немає.",
			"has_birthdays": "Дні народження:",
			"no_secret_channels": "Секретних каналів немає.",
			"has_secret_channels": "Секретні канали:",
			"happy_birthday": "<@%d>, з днем народження!",
			"no_access": "Ви не можете використовувати цю команду.",
			"import": "Дані імпортовані.",
			"invalid_arg": "Неприпустимі аргументи.",
			"invalid_format": "Неприпустимий формат аргументів.",
			"set_language": "Встановлено мову: %s.",
			"set_chance": "Встановлено шанс повідомлення: %d.",
			"current_language": "Поточна мова інтерфейсу: %s.",
			"current_chance_message": "Поточний шанс повідомлення: %d.",
			"current_chance_emoji": "Поточний шанс емодзі: %d.",
			"added_manager": "Додано керівницьку роль: <@&%d>.",
			"added_channel": "Додано секретний канал: <#%d>.",
			"added_birthday": "Додано день народження: <@%d>, %s.",
			"removed_manager": "Видалено керівницьку роль: <@&%d>.",
			"removed_channel": "Видалено секретний канал: <#%d>.",
			"removed_birthday": "Видалено день народження: <@%d>.",
			"cleared_managers": "Керівницькі ролі сервера очищено.",
			"cleared_channels": "Секретні канали сервера очищено.",
			"cleared_birthdays": "Дні народження сервера очищено.",
			"cleared_data": "Дані сервера очищено.",
			"cleared_messages": "Повідомлення сервера очищено.",
			"game_yes_1": "Так.",
			"game_yes_2": "Можеш не сумніватися в цьому)",
			"game_yes_3": "Саме ось так!",
			"game_yes_4": "Я впевнений у цьому на всі сто відсотків!",
			"game_no_1": "Ні.",
			"game_no_2": "Я вважаю, що ні)",
			"game_no_3": "Однозначно ні!",
			"game_no_4": "Ні, я впевнений у цьому на всі сто відсотків.",
			"msg_access": "Доступ",
			"msg_error": "Помилка",
			"msg_success": "Успіх",
			"january": "%d січня",
			"february": "%d лютого",
			"march": "%d березня",
			"april": "%d квітня",
			"may": "%d травня",
			"june": "%d червня",
			"july": "%d липня",
			"august": "%d серпня",
			"september": "%d вересня",
			"october": "%d жовтня",
			"november": "%d листопада",
			"december": "%d грудня",
			"user_birthday": "<@%d>: %s",
			"secret_channel": "<#%d>",
			"manager": "<@&%d>",
			"choice_set": "Мені запропонували вибрати один елемент з набору: %s",
			"choice_select": "Я вибираю... %s",
			"be": "білоруська",
			"en": "англійська",
			"ru": "російська",
			"uk": "українська"
		}
	""".trimIndent()
}