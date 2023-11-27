package hummel.service.impl

import hummel.bean.Settings
import hummel.service.LoginService
import org.javacord.api.DiscordApi
import org.javacord.api.interaction.SlashCommand
import org.javacord.api.interaction.SlashCommandOption
import org.javacord.api.interaction.SlashCommandOptionType

class LoginServiceImpl : LoginService {
	override fun registerCommands(api: DiscordApi) {
		"8ball" with Settings("/8ball [your question]", argsList(), api)
		"choice" with Settings("/choice [one] [two] [three]", argsList(), api)
		"complete" with Settings("/complete [text]", argsList(), api)
		"nuke" with Settings("/nuke [number]", argsList(), api)
		"random" with Settings("/random [number]", argsList(), api)
		"set_chance" with Settings("/set_chance [number]", argsList(), api)
		"set_language" with Settings("/set_language [ru/en]", argsList(), api)
		"add_birthday" with Settings("/add_birthday [user_id] [month_number] [day_number]", argsList(), api)
		"add_officer" with Settings("/add_officer [role_id]", argsList(), api)
		"add_general" with Settings("/add_general [role_id]", argsList(), api)
		"add_secret_channel" with Settings("/add_add_secret_channel [channel_id]", argsList(), api)
		"clear_birthdays" with Settings("/clear_birthdays {user_id}", argsList(false), api)
		"clear_officers" with Settings("/clear_officers {role_id}", argsList(false), api)
		"clear_generals" with Settings("/clear_generals {role_id}", argsList(false), api)
		"clear_secret_channels" with Settings("/clear_secret_channels {channel_id}", argsList(false), api)
		"clear_messages" with Settings("/clear_messages", emptyList(), api)
		"get_messages" with Settings("/get_messages", emptyList(), api)
		"get_data" with Settings("/get_data", emptyList(), api)
		"get_birthdays" with Settings("/get_birthdays", emptyList(), api)
		"get_commands" with Settings("/get_commands", emptyList(), api)
		"exit" with Settings("/exit", emptyList(), api)
		"shutdown" with Settings("/shutdown", emptyList(), api)

		//SlashCommandUpdater(1164890540798648361).setName("set_language").updateGlobal(api).join()
	}

	private infix fun String.with(settings: Settings) {
		SlashCommand.with(this, settings.usage, settings.args).createGlobal(settings.api)
	}

	private fun argsList(required: Boolean = true): List<SlashCommandOption> {
		return listOf(
			SlashCommandOption.create(
				SlashCommandOptionType.STRING, "Arguments", "The list of arguments", required
			)
		)
	}
}