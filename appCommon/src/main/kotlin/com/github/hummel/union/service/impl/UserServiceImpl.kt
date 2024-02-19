package com.github.hummel.union.service.impl

import com.github.hummel.union.bean.ApiResponse
import com.github.hummel.union.factory.ServiceFactory
import com.github.hummel.union.service.DataService
import com.github.hummel.union.service.UserService
import com.github.hummel.union.lang.I18n
import com.github.hummel.union.utils.error
import com.github.hummel.union.utils.random
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

class UserServiceImpl : UserService {
	private val dataService: DataService = ServiceFactory.dataService

	private val answers: Set<String> = setOf(
		"GAME_YES_1",
		"GAME_YES_2",
		"GAME_YES_3",
		"GAME_YES_4",
		"GAME_NO_1",
		"GAME_NO_2",
		"GAME_NO_3",
		"GAME_NO_4"
	)

	override fun answer(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("answer")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val arguments = sc.arguments[0].stringValue.get()
				val embed = if (arguments.contains("?")) {
					val answer = I18n.of(answers.random(), serverData)
					EmbedBuilder().success(sc, serverData, "— $arguments\r\n— $answer")
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("INVALID_ARG", serverData))
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
					EmbedBuilder().success(sc, serverData, "$arguments\r\n${arguments.random()}")
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("INVALID_ARG", serverData))
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

				val arguments = sc.arguments[0].stringValue.get().replace("\"", "\\\"")
				val embed = if (arguments.isNotEmpty()) {
					HttpClients.createDefault().use { client ->
						val request = HttpPost("https://api.porfirevich.com/generate/")

						val payload = """{ "prompt": "$arguments", "model": "xlarge", "length": 30 }"""
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

								EmbedBuilder().success(sc, serverData, "$arguments${apiResponse.replies.random()}")
							} else {
								EmbedBuilder().error(sc, serverData, I18n.of("NO_CONNECTION", serverData))
							}
						}
					}
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("INVALID_ARG", serverData))
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
							sc,
							serverData,
							I18n.of("RANDOM", serverData).format(random.nextInt(int))
						)
					} catch (e: Exception) {
						EmbedBuilder().error(sc, serverData, I18n.of("INVALID_FORMAT", serverData))
					}
				} else {
					EmbedBuilder().error(sc, serverData, I18n.of("INVALID_ARG", serverData))
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
					append(I18n.of("CURRENT_CHANCE", serverData).format(serverData.chance), "\r\n")
					append(I18n.of("CURRENT_LANGUAGE", serverData).format(serverData.lang), "\r\n")
					if (serverData.birthdays.isEmpty()) {
						append(I18n.of("NO_BIRTHDAYS", serverData), "\r\n")
					} else {
						serverData.birthdays.sortedWith(
							compareBy({ it.date.month }, { it.date.day })
						).joinTo(this, "\r\n") {
							val userId = server.getMemberById(it.id).get().id
							val month = Month.of(it.date.month)
							val day = it.date.day
							val date = I18n.of(month.name.uppercase(), serverData).format(day)

							I18n.of("birthday", serverData).format(userId, date)
						}
					}
				}
				val embed = EmbedBuilder().success(sc, serverData, text)
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(sc.server.get(), serverData)
			}.get()
		}
	}
}