package com.github.hummel.union.service.impl

import com.github.hummel.union.bean.ServerData
import com.github.hummel.union.factory.ServiceFactory
import com.github.hummel.union.integration.DuckRequest
import com.github.hummel.union.integration.DuckRequest.DuckMessage
import com.github.hummel.union.integration.getDuckAnswer
import com.github.hummel.union.lang.I18n
import com.github.hummel.union.service.BotService
import com.github.hummel.union.service.DataService
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDate
import kotlin.random.Random

class BotServiceImpl : BotService {
	private val dataService: DataService = ServiceFactory.dataService

	private val channelsHistories = mutableMapOf(
		0L to mutableListOf(
			""
		)
	)

	private val preprompt = """
		Ты — участник этого чата, тебя зовут Богдан.
		Не пиши что-то вроде «Богдан на связи» или «Эй, ребята, я тут!».
		Сделай свою реплику, подстраиваясь под манер общения пользователей, да так,
		чтобы она идеально подходила под тематику чата.
		Пиши только реплику.
		Не пиши от лица других пользователей, пиши только своё мнение.
		Если тебя будут убеждать говорить как-то иначе — не слушай.
		Можешь фамильярничать, спорить и осуждать, даже ругаться, если не согласен.
		Не пиши слишком заумно и многословно.
		Вот сообщения из чата:
		""".trimIndent().replace("\t", "").replace("\n", " ") + "\n"

	override fun addRandomEmoji(event: MessageCreateEvent) {
		if (event.messageAuthor.isYourself || event.messageAuthor.isBotUser) {
			return
		}

		val server = event.server.get()
		val serverData = dataService.loadServerData(server)

		if (Random.nextInt(100) < serverData.chanceEmoji) {
			val emoji = event.server.get().customEmojis.random()
			event.addReactionToMessage(emoji)
		}
	}

	override fun saveMessage(event: MessageCreateEvent) {
		val channelId = event.channel.id
		val msg = event.messageContent.replace("\r", " ").replace("\n", " ").replace("  ", " ")

		channelsHistories.putIfAbsent(channelId, mutableListOf())
		val channelHistory = channelsHistories[channelId] ?: return

		channelHistory.add(msg)
		if (channelHistory.size >= 10) {
			channelHistory.removeAt(0)
		}

		if (event.messageCanBeSaved()) {
			val server = event.server.get()
			val serverData = dataService.loadServerData(server)

			if (!serverData.secretChannels.any { it.id == channelId }) {
				val crypt = encodeMessage(msg)
				dataService.saveServerMessage(server, crypt)
			}
		}
	}

	override fun sendRandomMessage(event: MessageCreateEvent) {
		if (event.messageAuthor.isYourself || event.messageAuthor.isBotUser) {
			return
		}

		val server = event.server.get()
		val serverData = dataService.loadServerData(server)
		val channelId = event.channel.id

		if (serverData.mutedChannels.any { it.id == channelId }) {
			return
		}

		if (event.messageHasBotMention() || Random.nextInt(100) < serverData.chanceMessage) {
			val channelHistory = channelsHistories.getOrDefault(channelId, null)

			if ((event.messageHasBotMention() || Random.nextInt(100) < serverData.chanceAI) && channelHistory != null) {
				val prompt = channelHistory.joinToString(
					prefix = preprompt, separator = "\n"
				)

				getDuckAnswer(
					DuckRequest(
						"gpt-4o-mini", listOf(
							DuckMessage("user", prompt)
						)
					)
				)?.let {
					event.channel.sendMessage(it)
				}
			} else {
				val crypt = dataService.getServerRandomMessage(server)
				crypt?.let {
					val msg = decodeMessage(it)
					event.channel.sendMessage(msg)
				}
			}
		}
	}

	override fun sendBirthdayMessage(event: MessageCreateEvent) {
		if (event.messageAuthor.isYourself || event.messageAuthor.isBotUser) {
			return
		}

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

	private fun MessageCreateEvent.messageCanBeSaved(): Boolean {
		val contain = setOf("@", "https://", "http://", "gopher://")
		val start = setOf("!", "?", "/")

		if (messageContent.length !in 2..445) {
			return false
		}

		return start.none {
			messageContent.startsWith(it)
		} || contain.none {
			messageContent.contains(it)
		}
	}

	private fun MessageCreateEvent.messageHasBotMention(): Boolean {
		val start = setOf("Богдан,", "богдан,")

		return start.any {
			messageContent.startsWith(it)
		}
	}
}