package hummel.functions

import hummel.structures.ServerData
import hummel.utils.*
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
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.isNotEmpty()) {
			val embed = EmbedBuilder().empty(sc, data, Lang.NOT_AVAILABLE.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
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