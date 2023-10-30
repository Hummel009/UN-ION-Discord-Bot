package hummel.functions

import com.google.gson.Gson
import hummel.structures.ApiResponse
import hummel.structures.ServerData
import hummel.utils.*
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import kotlin.random.Random

val answers: Set<Lang> = setOf(
	Lang.GAME_YES_1,
	Lang.GAME_YES_2,
	Lang.GAME_YES_3,
	Lang.GAME_YES_4,
	Lang.GAME_NO_1,
	Lang.GAME_NO_2,
	Lang.GAME_NO_3,
	Lang.GAME_NO_4
)

fun eightBall(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("8ball")) {
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.isNotEmpty()) {
			sc.createImmediateResponder().setContent(answers.random().get(data)).respond()
		} else {
			val embed = EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		}
	}
}

fun choice(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("choice")) {
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.isNotEmpty()) {
			sc.createImmediateResponder().setContent(arguments.random()).respond()
		} else {
			val embed = EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		}
	}
}

fun complete(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("complete")) {
		val text = sc.arguments[0].stringValue.get()
		if (text.isNotEmpty()) {
			HttpClients.createDefault().use { client ->
				val request = HttpPost("https://api.porfirevich.com/generate/")

				val payload = """{ "prompt": "$text", "model": "xlarge", "length": 30 }"""
				request.entity = StringEntity(payload, ContentType.APPLICATION_JSON)

				request.addHeader("Accept", "*/*")
				request.addHeader("Accept-Encoding", "gzip, deflate, br")
				request.addHeader("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7,uk;q=0.6")

				client.execute(request).use { response ->
					if (response.statusLine.statusCode in 200..299) {
						val entity = response.entity
						val jsonResponse = EntityUtils.toString(entity)

						val gson = Gson()
						val apiResponse = gson.fromJson(jsonResponse, ApiResponse::class.java)

						val embed = EmbedBuilder().success(sc, data, "$text${apiResponse.replies.random()}")
						sc.respondLater().thenAccept {
							sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
						}
					} else {
						val embed = EmbedBuilder().error(sc, data, Lang.NO_CONNECTION.get(data))
						sc.respondLater().thenAccept {
							sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
						}
					}
				}
			}
		} else {
			val embed = EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		}
	}
}

fun setLanguage(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("set_language")) {
		if (!event.isGeneralMessage(data)) {
			val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			if (arguments.size == 1) {
				try {
					val lang = arguments[0]
					if (lang != "ru" && lang != "en") {
						throw Exception()
					}
					data.lang = lang
					val embed = EmbedBuilder().success(sc, data, "${Lang.SET_LANGUAGE.get(data)}: $lang")
					sc.respondLater().thenAccept {
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					}
				} catch (e: Exception) {
					val embed = EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
					sc.respondLater().thenAccept {
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					}
				}
			} else {
				val embed = EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				sc.respondLater().thenAccept {
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}
			}
		}
	}
}

fun random(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("random")) {
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.size == 1) {
			try {
				val long = arguments[0].toLong()
				val embed = EmbedBuilder().success(sc, data, "${Lang.RANDOM.get(data)}: ${Random.nextLong(long)}")
				sc.respondLater().thenAccept {
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}
			} catch (e: Exception) {
				val embed = EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
				sc.respondLater().thenAccept {
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}
			}
		} else {
			val embed = EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		}
	}
}