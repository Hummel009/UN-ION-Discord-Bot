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

	var lastBirthdayWishTime: Long = 0
	val twentyFourHoursInMillis = 24 * 60 * 60 * 1000

	api.addMessageCreateListener { event ->
		val serverID = event.server.get().id.toString()

		val data = readDataFromJson("$serverID/data.json") ?: getData(event, serverID)

		if (event.messageAuthor.isBotOwner) {
			registerClearDatabaseFunc(event, data)
			registerClearBirthdaysFunc(event, data)
			registerBackupDatabaseFunc(event, data)
			registerMessageChanceFunc(event, data)
			registerGetInfoFunc(event, data)
			registerAddBirthdayFunc(event, data)
		}
		if (!event.messageContent.isMessageForbidden() && !event.messageAuthor.isYourself && !event.messageAuthor.isBotUser) {
			saveMessage(event, data)
			sendMessage(event, data)

			val currentTime = System.currentTimeMillis()
			val timeSinceLastWish = currentTime - lastBirthdayWishTime

			val (isBirthday, userID) = isBirthdayToday(data)

			if (isBirthday && timeSinceLastWish >= twentyFourHoursInMillis) {
				sendBirthdayMessage(event, userID)
				lastBirthdayWishTime = currentTime
			}
		}

		saveDataToJson(data, "$serverID/data.json")
	}

	println("You can invite the bot by using the following url: " + api.createBotInvite())
}