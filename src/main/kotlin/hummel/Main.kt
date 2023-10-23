package hummel

import hummel.functions.*
import hummel.structures.Settings
import hummel.utils.getDataFromDiscord
import hummel.utils.isAllowedMessage
import hummel.utils.readDataFromJson
import hummel.utils.saveDataToJson
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

	"8ball" with Settings("/8ball [your question]", getArgs(), api)
	"choice" with Settings("/choice [one] [two] [three]", getArgs(), api)
	"complete" with Settings("/complete [text]", getArgs(), api)
	"nuke" with Settings("/nuke [number]", getArgs(), api)
	"random" with Settings("/random [number]", getArgs(), api)
	"set_chance" with Settings("/set_chance [number]", getArgs(), api)
	"set_language" with Settings("/set_language [ru/en]", getArgs(), api)
	"add_birthday" with Settings("/add_birthday [user_id] [month_number] [day_number]", getArgs(), api)
	"add_officer" with Settings("/add_officer [role_id]", getArgs(), api)
	"add_general" with Settings("/add_general [role_id]", getArgs(), api)
	"clear_birthdays" with Settings("/clear_birthdays {user_id}", getArgs(false), api)
	"clear_officers" with Settings("/clear_officers {role_id}", getArgs(false), api)
	"clear_generals" with Settings("/clear_generals {role_id}", getArgs(false), api)
	"clear_messages" with Settings("/clear_messages", emptyList(), api)
	"get_messages" with Settings("/get_messages", emptyList(), api)
	"get_data" with Settings("/get_data", emptyList(), api)
	"get_commands" with Settings("/get_commands", emptyList(), api)
	"exit" with Settings("/exit", emptyList(), api)
	"shutdown" with Settings("/shutdown", emptyList(), api)

	//SlashCommandUpdater(1164890540798648361).setName("set_language").updateGlobal(api).join()

	api.addInteractionCreateListener { event ->
		val serverID = event.interaction.server.get().id.toString()

		val data = readDataFromJson("$serverID/data.json") ?: getDataFromDiscord(event.interaction.server, serverID)

		eightBall(event, data)
		choice(event, data)
		random(event, data)
		complete(event, data)

		// OFFICER
		setChance(event, data)
		addBirthday(event, data)
		getServerMessages(event, data)
		getServerData(event, data)
		getCommands(event, data, api)

		//GENERAL
		clearServerMessages(event, data)
		clearServerBirthdays(event, data)
		clearServerGenerals(event, data)
		clearServerOfficers(event, data)
		addOfficer(event, data)
		addGeneral(event, data)
		nuke(event, data)
		setLanguage(event, data)

		//OWNER
		exit(event, data)
		shutdown(event, data)

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

private infix fun String.with(settings: Settings) {
	SlashCommand.with(this, settings.usage, settings.args).createGlobal(settings.api)
}

fun getArgs(required: Boolean = true): List<SlashCommandOption> {
	return listOf(
		SlashCommandOption.create(
			SlashCommandOptionType.STRING, "Arguments", "The list of arguments", required
		)
	)
}