package hummel.service.impl

import hummel.bean.BotData
import hummel.bean.Settings
import hummel.controller.impl.DiscordControllerImpl
import hummel.service.LoginService
import org.apache.hc.client5.http.classic.methods.HttpDelete
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.interaction.SlashCommand
import org.javacord.api.interaction.SlashCommandOption
import org.javacord.api.interaction.SlashCommandOptionType

class LoginServiceImpl : LoginService {
	override fun loginBot(impl: DiscordControllerImpl) {
		impl.api = DiscordApiBuilder().setToken(BotData.token).setAllIntents().login().join()
	}

	override fun deleteCommands(impl: DiscordControllerImpl) {
		impl.api.globalApplicationCommands.get().forEach { c ->
			val url = "https://discord.com/api/v10/applications/1147449520565801001/commands/${c.id}"
			HttpClients.createDefault().use { client ->
				val request = HttpDelete(url)

				request.setHeader("Authorization", "Bot ${BotData.token}")

				client.execute(request) { response ->
					println("${response.code}: ${response.reasonPhrase}")
				}
			}
		}
	}

	override fun registerCommands(impl: DiscordControllerImpl) {
		val api = impl.api
		"answer" with Settings("/answer [your question]", argsList(), api)
		"choice" with Settings("/choice [one] [two] [three]", argsList(), api)
		"complete" with Settings("/complete [text]", argsList(), api)
		"random" with Settings("/random [number]", argsList(), api)
		"info" with Settings("/info", emptyList(), api)

		"add_birthday" with Settings("/add_birthday [user_id] [month_number] [day_number]", argsList(), api)
		"add_manager" with Settings("/add_manager [role_id]", argsList(), api)
		"add_secret_channel" with Settings("/add_secret_channel [channel_id]", argsList(), api)
		"clear_birthdays" with Settings("/clear_birthdays {user_id}", argsList(false), api)
		"clear_managers" with Settings("/clear_managers {role_id}", argsList(false), api)
		"clear_secret_channels" with Settings("/clear_secret_channels {channel_id}", argsList(false), api)
		"clear_messages" with Settings("/clear_messages", emptyList(), api)
		"clear_data" with Settings("/clear_data", emptyList(), api)
		"set_chance" with Settings("/set_chance [number]", argsList(), api)
		"set_language" with Settings("/set_language [ru/en]", argsList(), api)
		"nuke" with Settings("/nuke [number]", argsList(), api)

		"import" with Settings("/import", file(), api)
		"export" with Settings("/export", emptyList(), api)
		"exit" with Settings("/exit", emptyList(), api)
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

	private fun file(required: Boolean = true): List<SlashCommandOption> {
		return listOf(
			SlashCommandOption.create(
				SlashCommandOptionType.ATTACHMENT, "Arguments", "The list of arguments", required
			)
		)
	}
}