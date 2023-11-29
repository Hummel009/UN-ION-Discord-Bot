package hummel.service.impl

import hummel.bean.ServerData
import hummel.dao.FileDao
import hummel.factory.DaoFactory
import hummel.service.BotService
import hummel.utils.Lang
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDate
import java.util.*

class BotServiceImpl : BotService {
	private val fileDao: FileDao = DaoFactory.fileDao

	override fun saveAllowedMessage(event: MessageCreateEvent, data: ServerData) {
		if (event.containsAllowedMessage()) {
			val channelId = event.channel.id
			if (!data.secretChannels.any { it.id == channelId }) {
				val msg = event.messageContent
				val crypt = encodeMessage(msg)

				val filePath = "${data.serverId}/messages.bin"
				fileDao.appendToFile(filePath, crypt.toByteArray())
				fileDao.appendToFile(filePath, "\r\n".toByteArray())
			}
		}
	}

	override fun sendRandomMessage(event: MessageCreateEvent, data: ServerData) {
		if (event.containsAllowedMessage()) {
			val rand = Random()
			val path = "${data.serverId}/messages.bin"

			if (rand.nextInt(data.chance) == 0) {
				val crypt = fileDao.getRandomLine(path)
				crypt?.let {
					val msg = decodeMessage(it)
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

			val (isBirthday, userIds) = isBirthdayToday(data)

			if (isBirthday && (currentDay != data.lastWish.day || currentMonth != data.lastWish.month)) {
				userIds.forEach { event.channel.sendMessage("<@$it>, ${Lang.HAPPY_BIRTHDAY.get(data)}!") }
				data.lastWish.day = currentDay
				data.lastWish.month = currentMonth
			}
		}
	}

	private fun encodeMessage(msg: String): String {
		return msg.codePoints().toArray().joinToString(" ")
	}

	private fun decodeMessage(msg: String): String {
		val unicodeCodes = msg.split(" ").map { it.toInt() }
		val unicodeChars = unicodeCodes.map { it.toChar() }.toCharArray()
		return String(unicodeChars)
	}

	private fun isBirthdayToday(data: ServerData): Pair<Boolean, Set<Long>> {
		val currentDate = LocalDate.now()
		val currentDay = currentDate.dayOfMonth
		val currentMonth = currentDate.monthValue
		val userIds = HashSet<Long>()
		var isBirthday = false

		for ((userId, date) in data.birthdays) {
			if (date.day == currentDay && date.month == currentMonth) {
				isBirthday = true
				userIds.add(userId)
			}
		}
		return isBirthday to userIds
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