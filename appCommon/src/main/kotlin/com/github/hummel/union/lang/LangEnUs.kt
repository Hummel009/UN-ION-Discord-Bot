package com.github.hummel.union.lang

object LangEnUs : Lang {
	override fun getJson(): String = """
		{
			"no_connection": "The site with the neural network is temporarily unavailable.",
			"exit": "Application will be turned off.",
			"nuke": "Messages have been deleted.",
			"random": "Random number: %d.",
			"no_managers": "There are no manager roles.",
			"has_managers": "Manager roles:",
			"no_birthdays": "There are no birthdays.",
			"has_birthdays": "Birthdays:",
			"no_secret_channels": "There are no secret channels.",
			"has_secret_channels": "Secret channels:",
			"happy_birthday": "<@%d>, happy birthday to you!",
			"no_access": "You cannot use this command.",
			"import": "Data has been imported.",
			"invalid_arg": "Invalid arguments.",
			"invalid_format": "Invalid arguments format.",
			"set_language": "Language has been set: %s.",
			"set_chance": "Message chance has been set: %s.",
			"current_language": "Current language: %s.",
			"current_chance": "Current chance: %d.",
			"added_manager": "Manager role has been added: <@&%d>.",
			"added_channel": "Secret channel has been added: <#%d>.",
			"added_birthday": "Birthday has been added: <@%d>, %s.",
			"removed_manager": "Manager role has been removed: <@&%d>.",
			"removed_channel": "Secret channel has been removed: <#%d>.",
			"removed_birthday": "Birthday has been removed: <@%d>.",
			"cleared_managers": "Server manager roles have been cleared.",
			"cleared_channels": "Server secret channels have been cleared.",
			"cleared_birthdays": "Server birthdays have been cleared.",
			"cleared_data": "Server data has been cleared.",
			"cleared_messages": "Server messages have been cleared.",
			"game_yes_1": "Yes.",
			"game_yes_2": "You can rest assured of that).",
			"game_yes_3": "Definitely yes!",
			"game_yes_4": "I'm 100% sure of it.",
			"game_no_1": "No.",
			"game_no_2": "I think not.",
			"game_no_3": "Definitely no!",
			"game_no_4": "Definitely not, I'm 100% sure.",
			"msg_access": "Access",
			"msg_error": "Error",
			"msg_success": "Success",
			"january": "January %d",
			"february": "February %d",
			"march": "March %d",
			"april": "April %d",
			"may": "May %d",
			"june": "June %d",
			"july": "July %d",
			"august": "August %d",
			"september": "September %d",
			"october": "October %d",
			"november": "November %d",
			"december": "December %d",
			"user_birthday": "<@%d>: %s",
			"secret_channel": "<#%d>",
			"manager": "<@&%d>",
			"choice_set": "I was asked to select one element from the set: %s",
			"choice_select": "I choose... %s",
			"be": "belarusian",
			"en": "english",
			"ru": "russian",
			"uk": "ukrainian"
		}
	""".trimIndent()
}