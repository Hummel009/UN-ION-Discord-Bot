package com.github.hummel.union.service.impl

import com.github.hummel.union.bean.ApiResponse
import com.github.hummel.union.bean.ServerData
import com.github.hummel.union.factory.ServiceFactory
import com.github.hummel.union.lang.I18n
import com.github.hummel.union.service.BotService
import com.github.hummel.union.service.DataService
import com.google.gson.Gson
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.entity.server.Server
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDate
import kotlin.random.Random
import kotlin.text.replace

class BotServiceImpl : BotService {
	private val dataService: DataService = ServiceFactory.dataService

	override fun addRandomEmoji(event: MessageCreateEvent) {
		if (event.containsAllowedMessage()) {
			val server = event.server.get()
			val serverData = dataService.loadServerData(server)

			if (Random.nextInt(serverData.chanceEmoji) == 0) {
				val emoji = event.server.get().customEmojis.random()
				event.addReactionToMessage(emoji)
			}
		}
	}

	override fun saveAllowedMessage(event: MessageCreateEvent) {
		if (event.containsAllowedMessage()) {
			val server = event.server.get()
			val serverData = dataService.loadServerData(server)

			val channelId = event.channel.id
			if (!serverData.secretChannels.any { it.id == channelId }) {
				val msg = event.messageContent
				val crypt = encodeMessage(msg)
				dataService.saveServerMessage(server, crypt)
			}
		}
	}

	override fun sendRandomMessage(event: MessageCreateEvent) {
		if (event.containsAllowedMessage()) {
			val server = event.server.get()
			val serverData = dataService.loadServerData(server)

			if (Random.nextInt(serverData.chanceMessage) == 0) {
				if (Random.nextInt(4) == 0) {
					val prompt = getPrompt(event.messageContent.replace("\"", "\\\""))
					val reply = HttpClients.createDefault().use { client ->
						val request = HttpPost("https://api.porfirevich.com/generate/")

						val payload = """
						{
							"prompt": "$prompt",
							"model": "xlarge",
							"length": 30,
							"temperature": 0.76
						}
						""".trimIndent()

						request.entity = StringEntity(payload, ContentType.APPLICATION_JSON)

						request.addHeader("Accept", "*/*")
						request.addHeader("Accept-Encoding", "gzip, deflate, br")
						request.addHeader("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7,uk;q=0.6")

						client.execute(request) { response ->
							if (response.code in 200..299) {
								val entity = response.entity
								val jsonResponse = EntityUtils.toString(entity)

								val gson = Gson()
								val apiResponse = gson.fromJson(jsonResponse, ApiResponse::class.java)

								cleanString(apiResponse.replies.random())
							} else {
								null
							}
						}
					}

					reply?.let {
						MessageBuilder().apply {
							append(reply)
							replyTo(event.message)
							send(event.channel)
						}
					} ?: run {
						getAndSend(server, event)
					}
				} else {
					getAndSend(server, event)
				}
			}
		}
	}

	override fun sendBirthdayMessage(event: MessageCreateEvent) {
		if (event.containsAllowedMessage()) {
			val server = event.server.get()
			val serverData = dataService.loadServerData(server)

			val currentDate = LocalDate.now()
			val currentDay = currentDate.dayOfMonth
			val currentMonth = currentDate.monthValue

			val (isBirthday, userIds) = isBirthdayToday(serverData)

			if (isBirthday && (currentDay != serverData.lastWish.day || currentMonth != serverData.lastWish.month)) {
				userIds.forEach { event.channel.sendMessage(I18n.of("happy_birthday", serverData).format(it)) }
				serverData.lastWish.day = currentDay
				serverData.lastWish.month = currentMonth
				dataService.saveServerData(server, serverData)
			}
		}
	}

	private fun cleanString(input: String): String? {
		val trimmedStart = input.trimStart { !isRussianUppercase(it) }

		val trimmedEnd = trimmedStart.trimEnd { !isRussianUppercase(it) }

		return if (trimmedEnd.isEmpty()) null else "$trimmedEnd!"
	}

	private fun isRussianUppercase(char: Char): Boolean =
		char in 'А'..'Я' || char in 'Ё'..'Ё' || char in 'а'..'я' || char in 'ё'..'ё'

	private fun getPrompt(input: String) =
		"Когда Владимиру Жириновскому сказали «$input», он прокомментировал это так: "

	private fun getAndSend(server: Server, event: MessageCreateEvent) {
		val crypt = dataService.getServerRandomMessage(server)
		crypt?.let {
			val msg = decodeMessage(it)
			event.channel.sendMessage(msg)
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