package com.github.hummel.union.service.impl

import com.github.hummel.union.bean.ApiResponse
import com.github.hummel.union.factory.ServiceFactory
import com.github.hummel.union.lang.I18n
import com.github.hummel.union.service.DataService
import com.github.hummel.union.service.UserService
import com.github.hummel.union.utils.error
import com.github.hummel.union.utils.success
import com.google.gson.Gson
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import java.time.Month
import kotlin.random.Random

class UserServiceImpl : UserService {
	private val dataService: DataService = ServiceFactory.dataService

	private val answers: Set<String> = setOf(
		"GAME_YES_1", "GAME_YES_2", "GAME_YES_3", "GAME_YES_4", "GAME_NO_1", "GAME_NO_2", "GAME_NO_3", "GAME_NO_4"
	)

	override fun answer(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("answer")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val arguments = sc.arguments[0].stringValue.get()
				val embed = if (arguments.contains("?")) {
					val answer = I18n.of(answers.random().lowercase(), serverData)
					EmbedBuilder().success(sc, serverData, "— $arguments\r\n— $answer")
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("invalid_arg", serverData))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun choice(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("choice")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val arguments = sc.arguments[0].stringValue.get().split(" ")
				val embed = if (arguments.isNotEmpty()) {
					val choiceSet = I18n.of("choice_set", serverData).format(arguments)
					val choiceSelect = I18n.of("choice_select", serverData).format(arguments.random())
					EmbedBuilder().success(sc, serverData, "$choiceSet\r\n$choiceSelect")
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("invalid_arg", serverData))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
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
					}
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("invalid_arg", serverData))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun random(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("random")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val arguments = sc.arguments[0].stringValue.get().split(" ")
				val embed = if (arguments.size == 1) {
					try {
						val int = arguments[0].toInt()
						EmbedBuilder().success(
							sc, serverData, I18n.of("random", serverData).format(Random.nextInt(int))
						)
					} catch (_: Exception) {
						EmbedBuilder().error(sc, serverData, I18n.of("invalid_format", serverData))
					}
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("invalid_arg", serverData))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun info(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("info")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				serverData.birthdays.removeIf { !server.getMemberById(it.id).isPresent }
				val text = buildString {
					val langName = I18n.of(serverData.lang, serverData)
					append(I18n.of("current_chance", serverData).format(serverData.chanceMessage), "\r\n")
					append(I18n.of("current_language", serverData).format(langName), "\r\n")
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
				}
				val embed = EmbedBuilder().success(sc, serverData, text)
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(sc.server.get(), serverData)
			}.get()
		}
	}
}