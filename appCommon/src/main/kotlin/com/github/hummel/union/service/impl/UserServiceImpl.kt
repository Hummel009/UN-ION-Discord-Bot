package com.github.hummel.union.service.impl

import com.github.hummel.union.bean.ApiResponse
import com.github.hummel.union.bean.ApiResponseDDG
import com.github.hummel.union.factory.ServiceFactory
import com.github.hummel.union.lang.I18n
import com.github.hummel.union.service.DataService
import com.github.hummel.union.service.UserService
import com.github.hummel.union.utils.error
import com.github.hummel.union.utils.success
import com.google.gson.Gson
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.net.URIBuilder
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import java.time.Month

class UserServiceImpl : UserService {
	private val dataService: DataService = ServiceFactory.dataService
	private val gson = Gson()

	private val personalHistory = mutableMapOf(
		0L to mutableMapOf(
			0L to mutableListOf(
				mapOf("" to "", "" to "")
			)
		)
	)

	override fun info(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("info")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				serverData.birthdays.removeIf { !server.getMemberById(it.id).isPresent }
				val text = buildString {
					val langName = I18n.of(serverData.lang, serverData)
					append(I18n.of("current_language", serverData).format(langName), "\r\n")
					append(I18n.of("current_chance_message", serverData).format(serverData.chanceMessage), "\r\n")
					append(I18n.of("current_chance_emoji", serverData).format(serverData.chanceEmoji), "\r\n")
					append(I18n.of("current_chance_ai", serverData).format(serverData.chanceAI), "\r\n")
					if (serverData.birthdays.isEmpty()) {
						append("\r\n", I18n.of("no_birthdays", serverData), "\r\n")
					} else {
						append("\r\n", I18n.of("has_birthdays", serverData), "\r\n")
						serverData.birthdays.sortedWith(
							compareBy({ it.date.month }, { it.date.day })
						).joinTo(this, "\r\n") {
							val userId = it.id
							val month = Month.of(it.date.month)
							val day = it.date.day
							val date = I18n.of(month.name.lowercase(), serverData).format(day)

							I18n.of("user_birthday", serverData).format(userId, date)
						}
						append("\r\n")
					}
					if (serverData.managers.isEmpty()) {
						append("\r\n", I18n.of("no_managers", serverData), "\r\n")
					} else {
						append("\r\n", I18n.of("has_managers", serverData), "\r\n")
						serverData.managers.sortedWith(compareBy { it.id }).joinTo(this, "\r\n") {
							val userId = it.id

							I18n.of("manager", serverData).format(userId)
						}
						append("\r\n")
					}
					if (serverData.secretChannels.isEmpty()) {
						append("\r\n", I18n.of("no_secret_channels", serverData), "\r\n")
					} else {
						append("\r\n", I18n.of("has_secret_channels", serverData), "\r\n")
						serverData.secretChannels.sortedWith(compareBy { it.id }).joinTo(this, "\r\n") {
							val channelId = it.id

							I18n.of("secret_channel", serverData).format(channelId)
						}
						append("\r\n")
					}
					if (serverData.mutedChannels.isEmpty()) {
						append("\r\n", I18n.of("no_muted_channels", serverData), "\r\n")
					} else {
						append("\r\n", I18n.of("has_muted_channels", serverData), "\r\n")
						serverData.mutedChannels.sortedWith(compareBy { it.id }).joinTo(this, "\r\n") {
							val channelId = it.id

							I18n.of("muted_channel", serverData).format(channelId)
						}
						append("\r\n")
					}
				}
				val embed = EmbedBuilder().success(sc, serverData, text)
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(sc.server.get(), serverData)
			}.get()
		}
	}

	override fun complete(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("complete")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val prompt = sc.arguments[0].stringValue.get().replace("\"", "\\\"")
				val embed = if (prompt.isNotEmpty()) {
					HttpClients.createDefault().use { client ->
						try {
							val request = HttpPost("https://api.porfirevich.com/generate/")

							val payload = """
							{
								"prompt": "$prompt",
								"model": "xlarge",
								"length": 100
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

									EmbedBuilder().success(sc, serverData, "$prompt${apiResponse.replies.random()}")
								} else {
									EmbedBuilder().error(sc, serverData, I18n.of("no_connection", serverData))
								}
							}
						} catch (e: Exception) {
							e.printStackTrace()

							EmbedBuilder().error(sc, serverData, I18n.of("invalid_format", serverData))
						}
					}
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("invalid_arg", serverData))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun aiAnswer(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("ai_answer")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val prompt = sc.arguments[0].stringValue.get()
				val embed = if (prompt.isNotEmpty()) {
					HttpClients.createDefault().use { client ->
						try {
							val channelId = sc.channel.get().id
							val authorId = sc.user.id

							val history = personalHistory.getOrDefault(channelId, null)?.getOrDefault(authorId, null)

							val url = URIBuilder("https://duck.gpt-api.workers.dev/chat/").apply {
								addParameter("prompt", prompt)
								history?.let { addParameter("history", gson.toJson(it)) }
							}.build().toString()

							personalHistory.putIfAbsent(channelId, mutableMapOf())
							personalHistory[channelId]!!.putIfAbsent(authorId, mutableListOf())
							personalHistory[channelId]!![authorId]!!.add(mapOf("role" to "user", "content" to prompt))

							val request = HttpGet(url)

							client.execute(request) { response ->
								if (response.code in 200..299) {
									val entity = response.entity
									val jsonResponse = EntityUtils.toString(entity)

									val gson = Gson()
									val apiResponse = gson.fromJson(jsonResponse, ApiResponseDDG::class.java)


									EmbedBuilder().success(sc, serverData, apiResponse.response)
								} else {
									EmbedBuilder().error(sc, serverData, I18n.of("no_connection", serverData))
								}
							}
						} catch (e: Exception) {
							e.printStackTrace()

							EmbedBuilder().error(sc, serverData, I18n.of("invalid_format", serverData))
						}
					}
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("invalid_arg", serverData))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun aiClear(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("ai_clear")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val channelId = sc.channel.get().id
				val authorId = sc.user.id

				personalHistory.put(
					channelId, mutableMapOf(
						authorId to mutableListOf(
							mapOf()
						)
					)
				)

				val embed = EmbedBuilder().success(sc, serverData, I18n.of("ai_clear", serverData))

				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}
}