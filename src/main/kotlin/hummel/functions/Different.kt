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
		sc.respondLater().thenAccept {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			val embed = if (arguments.isNotEmpty()) {
				EmbedBuilder().success(sc, data, answers.random().get(data))
			} else {
				EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}
	}
}

fun choice(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("choice")) {
		sc.respondLater().thenAccept {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			val embed = if (arguments.isNotEmpty()) {
				EmbedBuilder().success(sc, data, arguments.random())
			} else {
				EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}
	}
}

fun complete(event: InteractionCreateEvent, data: ServerData) {
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
						if (response.statusLine.statusCode in 200..299) {
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
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}
	}
}

fun setLanguage(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("set_language")) {
		sc.respondLater().thenAccept {
			val embed = if (!event.isGeneralMessage(data)) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				if (arguments.size == 1) {
					try {
						val lang = arguments[0]
						if (lang != "ru" && lang != "en") {
							throw Exception()
						}
						data.lang = lang
						EmbedBuilder().success(sc, data, "${Lang.SET_LANGUAGE.get(data)}: $lang")
					} catch (e: Exception) {
						EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
					}
				} else {
					EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				}
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}
	}
}

fun random(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("random")) {
		sc.respondLater().thenAccept {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			val embed = if (arguments.size == 1) {
				try {
					val long = arguments[0].toLong()
					EmbedBuilder().success(sc, data, "${Lang.RANDOM.get(data)}: ${Random.nextLong(long)}")
				} catch (e: Exception) {
					EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
				}
			} else {
				EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}
	}
}