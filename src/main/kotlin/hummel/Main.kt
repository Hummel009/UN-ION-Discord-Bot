package hummel

import hummel.functions.*
import hummel.utils.*
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.intent.Intent
import org.javacord.api.interaction.SlashCommand
import org.javacord.api.interaction.SlashCommandOption
import org.javacord.api.interaction.SlashCommandOptionType
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

val rand: Random = Random()

fun main() {
	val token = File("token.txt").readText(StandardCharsets.UTF_8)
	val api = DiscordApiBuilder().setToken(token).addIntents(*Intent.values()).login().join()

	SlashCommand.with(
		"8ball", "/8ball [your question]", listOf(
			SlashCommandOption.create(
				SlashCommandOptionType.STRING, "Arguments", "The list of arguments", true
			)
		)
	).createGlobal(api)

	SlashCommand.with(
		"choice", "/choice [one] [two] [three]", listOf(
			SlashCommandOption.create(
				SlashCommandOptionType.STRING, "Arguments", "The list of arguments", true
			)
		)
	).createGlobal(api)

	SlashCommand.with(
		"set_chance", "/set_chance [number]", listOf(
			SlashCommandOption.create(
				SlashCommandOptionType.STRING, "Arguments", "The list of arguments", true
			)
		)
	).createGlobal(api)

	SlashCommand.with(
		"add_birthday", "/add_birthday [user_id] [month_number] [day_number]", listOf(
			SlashCommandOption.create(
				SlashCommandOptionType.STRING, "Arguments", "The list of arguments", true
			)
		)
	).createGlobal(api)

	SlashCommand.with("clear_messages", "/clear_messages").createGlobal(api)

	SlashCommand.with(
		"clear_birthdays", "/clear_birthdays {user_id}", listOf(
			SlashCommandOption.create(
				SlashCommandOptionType.STRING, "Arguments", "The list of arguments", false
			)
		)
	).createGlobal(api)

	SlashCommand.with("get_messages", "/get_messages").createGlobal(api)
	SlashCommand.with("get_data", "/get_data").createGlobal(api)

	api.addInteractionCreateListener { event ->
		val serverID = event.interaction.server.get().id.toString()

		val data = readDataFromJson("$serverID/data.json") ?: getDataFromDiscord(event.interaction.server, serverID)

		eightBall(event)
		randomChoice(event)

		if (event.isOfficerMessage()) {
			setChance(event, data)
			addBirthday(event, data)
		}

		if (event.isGeneralMessage()) {
			clearServerMessages(event, data)
			clearServerBirthdays(event, data)
			getServerMessages(event, data)
			getServerData(event, data)
		}

		saveDataToJson(data, "$serverID/data.json")
	}

	api.addMessageCreateListener { event ->
		val serverID = event.server.get().id.toString()

		val data = readDataFromJson("$serverID/data.json") ?: getDataFromDiscord(event.server, serverID)

		if (event.isAllowedMessage()) {
			saveAllowedMessage(event, data)
			sendRandomMessage(event, data)
			sendBirthdayMessage(event, data)
		}

		saveDataToJson(data, "$serverID/data.json")
	}

	println("You can invite the bot by using the following url: " + api.createBotInvite())
}