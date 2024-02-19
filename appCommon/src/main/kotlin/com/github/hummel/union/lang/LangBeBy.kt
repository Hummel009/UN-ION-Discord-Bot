package com.github.hummel.union.lang

object LangBeBy : Lang {
	override fun getJson(): String = """
		{
			"no_connection": "Сайт з нейрасецівам тэрмінова недаступны.",
			"exit": "Прыкладанне будзе выключана.",
			"nuke": "Паведамленні выдалены.",
			"random": "Выпадковы лік: %d.",
			"no_birthdays": "Днёў нараджэння няма.",
			"happy_birthday": "<@%d>, з днём нараджэння!",
			"no_access": "Вы не можаце выкарыстоўваць гэтую каманду.",
			"import": "Даныя імпартаваны.",
			"invalid_arg": "Недапушчальныя аргументы.",
			"invalid_format": "Недапушчальны фармат аргументаў.",
			"set_language": "Усталявана мова: %s.",
			"set_chance": "Усталяваны шанс паведамлення: %d.",
			"current_language": "Бягучая мова інтэрфейса: %s.",
			"current_chance": "Бягучы шанс паведамлення: %d.",
			"added_manager": "Дададзена кіраўніцкая роль: <@&%d>.",
			"added_channel": "Дададзены сакрэтны канал: <#%d>.",
			"added_birthday": "Дададзены дзень нараджэння: <@%d>, %s.",
			"removed_manager": "Выдалена кіраўніцкая роль: <@&%d>.",
			"removed_channel": "Выдалены сакрэтны канал: <#%d>.",
			"removed_birthday": "Выдалены дзень нараджэння: <@%d>.",
			"cleared_managers": "Кіраўніцкія ролі сервера ачышчаны.",
			"cleared_channels": "Сакрэтныя каналы сервера ачышчаны.",
			"cleared_birthdays": "Дні нараджэння сервера ачышчаны.",
			"cleared_data": "Даныя сервера ачышчаны.",
			"cleared_messages": "Паведамленні сервера ачышчаны.",
			"game_yes_1": "Так.",
			"game_yes_2": "Можаш не сумнявацца ў гэтым)",
			"game_yes_3": "Менавіта вось так!",
			"game_yes_4": "Я ўпэўнены ў гэтым на ўсе сто адсоткаў!",
			"game_no_1": "Не.",
			"game_no_2": "Я лічу, што не)",
			"game_no_3": "Адназначна не!",
			"game_no_4": "Не, я ўпэўнены ў гэтым на ўсе сто адсоткаў.",
			"msg_access": "Доступ",
			"msg_error": "Памылка",
			"msg_success": "Поспех",
			"january": "%d студзеня",
			"february": "%d лютага",
			"march": "%d сакавіка",
			"april": "%d красавіка",
			"may": "%d траўня",
			"june": "%d чэрвеня",
			"july": "%d ліпеня",
			"august": "%d жніўня",
			"september": "%d верасня",
			"october": "%d кастрычніка",
			"november": "%d лістапада",
			"december": "%d снежня",
			"birthday": "<@%d>: %s"
		}
	""".trimIndent()
}