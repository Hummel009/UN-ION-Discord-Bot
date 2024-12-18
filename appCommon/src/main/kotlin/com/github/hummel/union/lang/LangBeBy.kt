package com.github.hummel.union.lang

object LangBeBy : Lang {
	override fun getJson(): String = """
		{
			"ai_clear": "Кантэкст ачышчаны.",
			"no_connection": "Сайт з нейрасецівам тэрмінова недаступны.",
			"exit": "Прыкладанне будзе выключана.",
			"nuke": "Паведамленні выдалены.",
			"no_managers": "Кіраўніцкіх роляў няма.",
			"has_managers": "Кіраўніцкія ролі:",
			"no_birthdays": "Днёў нараджэння няма.",
			"has_birthdays": "Дні нараджэння:",
			"no_secret_channels": "Сакрэтных каналаў няма.",
			"has_secret_channels": "Сакрэтныя каналы:",
			"no_muted_channels": "Адключаных каналаў няма.",
			"has_muted_channels": "Адключаныя каналы:",
			"happy_birthday": "<@%d>, з днём нараджэння!",
			"no_access": "Вы не можаце выкарыстоўваць гэтую каманду.",
			"import": "Даныя імпартаваны.",
			"invalid_arg": "Недапушчальныя аргументы.",
			"invalid_format": "Недапушчальны фармат аргументаў.",
			"set_language": "Усталявана мова: %s.",
			"set_chance_message": "Усталяваны шанс паведамлення: %d.",
			"set_chance_emoji": "Усталяваны шанс эмадзі: %d.",
			"set_chance_ai": "Усталяваны шанс ШІ-паведамлення: %d.",
			"current_language": "Бягучая мова інтэрфейса: %s.",
			"current_chance_message": "Бягучы шанс паведамлення: %d.",
			"current_chance_emoji": "Бягучы шанс эмадзі: %d.",
			"current_chance_ai": "Бягучы шанс ШІ-паведамлення: %d.",
			"added_manager": "Дададзена кіраўніцкая роль: <@&%d>.",
			"added_secret_channel": "Дададзены сакрэтны канал: <#%d>.",
			"added_muted_channel": "Дададзены адключаны канал: <#%d>.",
			"added_birthday": "Дададзены дзень нараджэння: <@%d>, %s.",
			"removed_manager": "Выдалена кіраўніцкая роль: <@&%d>.",
			"removed_secret_channel": "Выдалены сакрэтны канал: <#%d>.",
			"removed_muted_channel": "Выдалены адключаны канал: <#%d>.",
			"removed_birthday": "Выдалены дзень нараджэння: <@%d>.",
			"cleared_managers": "Кіраўніцкія ролі сервера ачышчаны.",
			"cleared_secret_channels": "Сакрэтныя каналы сервера ачышчаны.",
			"cleared_muted_channels": "Адключаныя каналы сервера ачышчаны.",
			"cleared_birthdays": "Дні нараджэння сервера ачышчаны.",
			"cleared_data": "Даныя сервера ачышчаны.",
			"cleared_messages": "Паведамленні сервера ачышчаны.",
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
			"user_birthday": "<@%d>: %s",
			"secret_channel": "<#%d>",
			"muted_channel": "<#%d>",
			"manager": "<@&%d>",
			"be": "беларуская",
			"en": "англійская",
			"ru": "руская",
			"uk": "украінская"
		}
	""".trimIndent()
}