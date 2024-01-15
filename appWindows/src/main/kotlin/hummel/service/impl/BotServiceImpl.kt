package hummel.service.impl

import hummel.bean.ServerData
import hummel.dao.FileDao
import hummel.factory.DaoFactory
import hummel.factory.ServiceFactory
import hummel.service.BotService
import hummel.service.DataService
import hummel.utils.Lang
import hummel.utils.random
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDate

class BotServiceImpl : BotService {
	private val dataService: DataService = ServiceFactory.dataService
	private val fileDao: FileDao = DaoFactory.fileDao

	override fun addRandomEmoji(event: MessageCreateEvent) {
		if (event.containsAllowedMessage()) {
			val serverData = dataService.loadServerData(event.server.get())
			if (random.nextInt(serverData.chance) == 0) {
				val emoji = event.server.get().customEmojis.random()
				event.addReactionToMessage(emoji)
			}
		}
	}

	override fun saveAllowedMessage(event: MessageCreateEvent) {
		if (event.containsAllowedMessage()) {
			val serverData = dataService.loadServerData(event.server.get())
			val channelId = event.channel.id
			if (!serverData.secretChannels.any { it.id == channelId }) {
				val msg = event.messageContent
				val crypt = encodeMessage(msg)

				val filePath = "${serverData.serverId}/messages.bin"
				fileDao.appendToFile(filePath, crypt.toByteArray())
				fileDao.appendToFile(filePath, "\r\n".toByteArray())
			}
		}
	}

	override fun sendRandomMessage(event: MessageCreateEvent) {
		if (event.containsAllowedMessage()) {
			val serverData = dataService.loadServerData(event.server.get())
			val path = "${serverData.serverId}/messages.bin"

			if (random.nextInt(serverData.chance) == 0) {
				val crypt = fileDao.getRandomLine(path)
				crypt?.let {
					val msg = decodeMessage(it)
					event.channel.sendMessage(msg)
				}
			}
		}
	}

	override fun sendBirthdayMessage(event: MessageCreateEvent) {
		if (event.containsAllowedMessage()) {
			val serverData = dataService.loadServerData(event.server.get())
			val currentDate = LocalDate.now()
			val currentDay = currentDate.dayOfMonth
			val currentMonth = currentDate.monthValue

			val (isBirthday, userIds) = isBirthdayToday(serverData)

			if (isBirthday && (currentDay != serverData.lastWish.day || currentMonth != serverData.lastWish.month)) {
				userIds.forEach { event.channel.sendMessage("<@$it>, ${Lang.HAPPY_BIRTHDAY[serverData]}!") }
				serverData.lastWish.day = currentDay
				serverData.lastWish.month = currentMonth
			}
		}
	}

	private fun encodeMessage(msg: String): String = msg.codePoints().toArray().joinToString(" ")

	private fun decodeMessage(msg: String): String {
		val unicodeCodes = msg.split(" ").map { it.toInt() }
		val unicodeChars = unicodeCodes.map { it.toChar() }.toCharArray()
		return String(unicodeChars)
	}

	private fun isBirthdayToday(serverData: ServerData): Pair<Boolean, Set<Long>> {
		val currentDate = LocalDate.now()
		val currentDay = currentDate.dayOfMonth
		val currentMonth = currentDate.monthValue
		val userIds = HashSet<Long>()
		var isBirthday = false

		for ((userId, date) in serverData.birthdays) {
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