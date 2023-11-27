package hummel.service.impl

import hummel.bean.ServerData
import hummel.factory.DaoFactory
import hummel.factory.ServiceFactory
import hummel.service.BotService
import hummel.utils.Lang
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDate
import java.util.*

class BotServiceImpl : BotService {
	private val dao = DaoFactory.dao
	private val cryptService = ServiceFactory.cryptService

	override fun saveAllowedMessage(event: MessageCreateEvent, data: ServerData) {
		if (event.containsAllowedMessage()) {
			val channelID = event.channel.id
			if (!data.channels.any { it.channelID == channelID }) {
				val msg = event.messageContent
				val crypt = cryptService.encodeMessage(msg)

				val filePath = "${data.serverID}/messages.bin"
				dao.appendToFile(filePath, crypt.toByteArray())
				dao.appendToFile(filePath, "\r\n".toByteArray())
			}
		}
	}

	override fun sendRandomMessage(event: MessageCreateEvent, data: ServerData) {
		if (event.containsAllowedMessage()) {
			val rand = Random()
			val path = "${data.serverID}/messages.bin"

			if (rand.nextInt(data.chance) == 0) {
				val crypt = dao.getRandomLine(path)
				crypt?.let {
					val msg = cryptService.decodeMessage(it)
					event.channel.sendMessage(msg)
				}
			}
		}
	}

	override fun sendBirthdayMessage(event: MessageCreateEvent, data: ServerData) {
		if (event.containsAllowedMessage()) {
			val currentDate = LocalDate.now()
			val currentDay = currentDate.dayOfMonth
			val currentMonth = currentDate.monthValue

			val (isBirthday, userIDs) = isBirthdayToday(data)

			if (isBirthday && (currentDay != data.lastWish.day || currentMonth != data.lastWish.month)) {
				userIDs.forEach { event.channel.sendMessage("<@$it>, ${Lang.HAPPY_BIRTHDAY.get(data)}!") }
				data.lastWish.day = currentDay
				data.lastWish.month = currentMonth
			}
		}
	}

	private fun isBirthdayToday(data: ServerData): Pair<Boolean, Set<Long>> {
		val currentDate = LocalDate.now()
		val currentDay = currentDate.dayOfMonth
		val currentMonth = currentDate.monthValue
		val userIDs = HashSet<Long>()
		var isBirthday = false

		for ((userID, date) in data.birthdays) {
			if (date.day == currentDay && date.month == currentMonth) {
				isBirthday = true
				userIDs.add(userID)
			}
		}
		return isBirthday to userIDs
	}

	private fun MessageCreateEvent.containsAllowedMessage(): Boolean {
		val contain = setOf("@", "http", "\r", "\n")
		val start = setOf("!", "?", "/")

		if (start.any { messageContent.startsWith(it) } || contain.any { messageContent.contains(it) }) {
			return false
		}

		if (messageAuthor.isYourself || messageAuthor.isBotUser) {
			return false
		}

		return messageContent.length >= 2
	}
}