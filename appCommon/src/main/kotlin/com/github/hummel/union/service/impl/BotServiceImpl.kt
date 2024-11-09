package com.github.hummel.union.service.impl

import com.github.hummel.union.bean.ApiResponseDDG
import com.github.hummel.union.bean.ServerData
import com.github.hummel.union.factory.ServiceFactory
import com.github.hummel.union.lang.I18n
import com.github.hummel.union.service.BotService
import com.github.hummel.union.service.DataService
import com.google.gson.Gson
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.net.URIBuilder
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDate
import kotlin.collections.getOrDefault
import kotlin.random.Random

class BotServiceImpl : BotService {
	private val dataService: DataService = ServiceFactory.dataService
	private val gson = Gson()

	private val internalHistory = mutableMapOf(
		0L to mutableListOf(
			mapOf("" to "", "" to "")
		)
	)

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
				val crypt = dataService.getServerRandomMessage(server)
				crypt?.let {
					val msg = decodeMessage(it)
					event.channel.sendMessage(msg)
				}
			}
		}
	}

	override fun sendAIMessage(event: MessageCreateEvent) {
		if (event.startsWithBotClearMention()) {
			internalHistory.put(event.message.author.id, mutableListOf())
		} else if (event.startsWithBotMention()) {
			val prompt = event.messageContent
			val reply = HttpClients.createDefault().use { client ->
				val history = internalHistory.getOrDefault(event.message.author.id, null)

				val url = URIBuilder("https://duck.gpt-api.workers.dev/chat/").apply {
					addParameter("prompt", prompt)
					history?.let { addParameter("history", gson.toJson(it)) }
				}.build().toString()

				internalHistory.putIfAbsent(event.message.author.id, mutableListOf())
				internalHistory[event.message.author.id]!!.add(mapOf("role" to "user", "content" to prompt))

				val request = HttpGet(url)

				client.execute(request) { response ->
					if (response.code in 200..299) {
						val entity = response.entity
						val jsonResponse = EntityUtils.toString(entity)

						val gson = Gson()
						val apiResponse = gson.fromJson(jsonResponse, ApiResponseDDG::class.java)

						apiResponse.response
					} else {
						null
					}
				}
			}

			if (reply != null) {
				MessageBuilder().apply {
					append(reply)
					replyTo(event.message)
					send(event.channel)
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
		val start = setOf("!", "?", "/", "Богдан, ", "богдан, ")

		if (start.any { messageContent.startsWith(it) } || contain.any { messageContent.contains(it) }) {
			return false
		}

		if (messageAuthor.isYourself || messageAuthor.isBotUser) {
			return false
		}

		return messageContent.length >= 2
	}

	private fun MessageCreateEvent.startsWithBotMention(): Boolean {
		val start = setOf("Богдан, ", "богдан, ")

		if (messageAuthor.isYourself || messageAuthor.isBotUser) {
			return false
		}

		return start.any { messageContent.startsWith(it) }
	}

	private fun MessageCreateEvent.startsWithBotClearMention(): Boolean {
		val start = setOf("!Богдан", "!богдан")

		if (messageAuthor.isYourself || messageAuthor.isBotUser) {
			return false
		}

		return start.any { messageContent.startsWith(it) }
	}
}