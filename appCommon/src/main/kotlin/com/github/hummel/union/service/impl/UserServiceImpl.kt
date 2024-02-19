package com.github.hummel.union.service.impl

import com.google.gson.Gson
import com.github.hummel.union.bean.ApiResponse
import com.github.hummel.union.factory.ServiceFactory
import com.github.hummel.union.service.DataService
import com.github.hummel.union.service.UserService
import com.github.hummel.union.utils.*
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

	private val answers: Set<Lang> = setOf(
		Lang.GAME_YES_1,
		Lang.GAME_YES_2,
		Lang.GAME_YES_3,
		Lang.GAME_YES_4,
		Lang.GAME_NO_1,
		Lang.GAME_NO_2,
		Lang.GAME_NO_3,
		Lang.GAME_NO_4
	)

	override fun answer(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("answer")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val arguments = sc.arguments[0].stringValue.get()
				val embed = if (arguments.contains("?")) {
					EmbedBuilder().success(sc, serverData, "— $arguments\r\n— ${answers.random()[serverData]}")
				} else {
					EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
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
					EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
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
								EmbedBuilder().error(sc, serverData, Lang.NO_CONNECTION[serverData])
							}
						}
					}
				} else {
					EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
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
						EmbedBuilder().success(sc, serverData, "${Lang.RANDOM[serverData]}: ${random.nextInt(int)}")
					} catch (e: Exception) {
						EmbedBuilder().error(sc, serverData, Lang.INVALID_FORMAT[serverData])
					}
				} else {
					EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
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
					append(Lang.CURRENT_CHANCE[serverData], ": ", serverData.chance, "\r\n")
					append(Lang.CURRENT_LANGUAGE[serverData], ": ", serverData.lang, "\r\n")
					if (serverData.birthdays.isEmpty()) {
						append(Lang.NO_BIRTHDAYS[serverData], "\r\n")
					} else {
						serverData.birthdays.sortedWith(
							compareBy({ it.date.month }, { it.date.day })
						).joinTo(this, "\r\n") {
							val userId = server.getMemberById(it.id).get().id
							val month = Month.of(it.date.month)
							val day = it.date.day
							val format = getFormattedTranslatedDate(month, serverData, day)
							"<@$userId>: $format"
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