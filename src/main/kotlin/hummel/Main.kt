package hummel

import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.intent.Intent
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

val rand: Random = Random()

fun main() {
	val token = File("token.txt").readText(StandardCharsets.UTF_8)
	val api = DiscordApiBuilder().setToken(token).addIntents(*Intent.values()).login().join()

	api.addMessageCreateListener { event ->
		val serverID = event.server.get().id.toString()

		val data = readDataFromJson("$serverID/data.json") ?: getData(event, serverID)

		if (event.messageAuthor.isBotOwner) {
			registerClearFunc(event, data)
			registerBackupFunc(event, data)
			registerChanceFunc(event, data)
			registerGetInfoFunc(event, data)
			registerBirthdayFunc(event, data)
		}
		if (!event.messageContent.isMessageForbidden() && !event.messageAuthor.isYourself && !event.messageAuthor.isBotUser) {
			saveMessage(event, data)
			sendMessage(event, data)

			val birthdayAndWhose = isBirthdayToday(data)

			if (birthdayAndWhose.first) {
				sendBirthdayMessage(event, birthdayAndWhose.second)
			}
		}

		saveDataToJson(data, "$serverID/data.json")
	}

	println("You can invite the bot by using the following url: " + api.createBotInvite())
}