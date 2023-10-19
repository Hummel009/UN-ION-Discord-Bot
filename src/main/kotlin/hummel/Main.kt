package hummel

import hummel.functions.*
import hummel.utils.getDataFromDiscord
import hummel.utils.isMessageForbidden
import hummel.utils.readDataFromJson
import hummel.utils.saveDataToJson
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.intent.Intent
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

const val prefix: String = "!"

val rand: Random = Random()
val functions: MutableSet<String> = HashSet()

fun main() {
	val token = File("token.txt").readText(StandardCharsets.UTF_8)
	val api = DiscordApiBuilder().setToken(token).addIntents(*Intent.values()).login().join()

	var lastBirthdayWishTime: Long = 0
	val twentyFourHoursInMillis = 24 * 60 * 60 * 1000

	api.addMessageCreateListener { event ->
		val serverID = event.server.get().id.toString()

		val data = readDataFromJson("$serverID/data.json") ?: getDataFromDiscord(event, serverID)

		eightBall(event)
		randomChoice(event)
		getHelp(event)

		if (event.messageAuthor.canManageMessagesInTextChannel()) {
			setMessageChance(event, data)
			getServerMessages(event, data)
			getServerData(event, data)
			addBirthday(event, data)
		}

		if (event.messageAuthor.isBotOwner) {
			clearServerMessages(event, data)
			clearServerBirthdays(event, data)
		}

		if (!event.messageContent.isMessageForbidden() && !event.messageAuthor.isYourself && !event.messageAuthor.isBotUser) {
			saveAllowedMessage(event, data)
			sendRandomMessage(event, data)

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