package hummel.service.impl

import com.google.gson.Gson
import hummel.random
import hummel.bean.ApiResponse
import hummel.bean.ServerData
import hummel.service.UserService
import hummel.utils.Lang
import hummel.utils.error
import hummel.utils.success
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import java.time.Month

class UserServiceImpl : UserService {
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

	override fun answer(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("answer")) {
			sc.respondLater().thenAccept {
				val text = sc.arguments[0].stringValue.get()
				val embed = if (text.contains("?")) {
					EmbedBuilder().success(sc, data, "— $text\r\n— ${answers.random().get(data)}")
				} else {
					EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun choice(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("choice")) {
			sc.respondLater().thenAccept {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				val embed = if (arguments.isNotEmpty()) {
					EmbedBuilder().success(sc, data, "$arguments\r\n${arguments.random()}")
				} else {
					EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun complete(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("complete")) {
			sc.respondLater().thenAccept {
				val text = sc.arguments[0].stringValue.get()
				val embed = if (text.isNotEmpty()) {
					HttpClients.createDefault().use { client ->
						val request = HttpPost("https://api.porfirevich.com/generate/")

						val payload = """{ "prompt": "$text", "model": "xlarge", "length": 30 }"""
						request.entity = StringEntity(payload, ContentType.APPLICATION_JSON)

						request.addHeader("Accept", "*/*")
						request.addHeader("Accept-Encoding", "gzip, deflate, br")
						request.addHeader("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7,uk;q=0.6")

						client.execute(request).use { response ->
							if (response.code in 200..299) {
								val entity = response.entity
								val jsonResponse = EntityUtils.toString(entity)

								val gson = Gson()
								val apiResponse = gson.fromJson(jsonResponse, ApiResponse::class.java)

								EmbedBuilder().success(sc, data, "$text${apiResponse.replies.random()}")
							} else {
								EmbedBuilder().error(sc, data, Lang.NO_CONNECTION.get(data))
							}
						}
					}
				} else {
					EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun random(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("random")) {
			sc.respondLater().thenAccept {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				val embed = if (arguments.size == 1) {
					try {
						val int = arguments[0].toInt()
						EmbedBuilder().success(sc, data, "${Lang.RANDOM.get(data)}: ${random.nextInt(int)}")
					} catch (e: Exception) {
						EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
					}
				} else {
					EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun info(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("info")) {
			sc.respondLater().thenAccept {
				data.birthdays.removeIf { !sc.server.get().getMemberById(it.id).isPresent }
				val text = if (data.birthdays.isEmpty()) Lang.NO_BIRTHDAYS.get(data) else buildString {
					data.birthdays.sortedWith(compareBy({ it.date.month }, { it.date.day })).joinTo(this, "\r\n") {
						val userName = sc.server.get().getMemberById(it.id).get().name
						"$userName: ${it.date.day} ${Month.of(it.date.month)}"
					}
				}
				val embed = EmbedBuilder().success(sc, data, text)
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}
}