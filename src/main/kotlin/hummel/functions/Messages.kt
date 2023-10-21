package hummel.functions

import hummel.rand
import hummel.structures.ServerData
import hummel.utils.*
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import org.javacord.api.event.message.MessageCreateEvent
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

fun sendRandomMessage(event: MessageCreateEvent, data: ServerData) {
	val path = Paths.get("${data.serverID}/messages.bin")

	if (rand.nextInt(data.chance) == 0) {
		val randomLine = path.getRandomLine()
		randomLine?.let {
			event.channel.sendMessage(it)
		}
	}
}

fun saveAllowedMessage(event: MessageCreateEvent, data: ServerData) {
	val path = Paths.get("${data.serverID}/messages.bin")
	val ints = event.messageContent.codePoints().toArray()
	Files.write(
		path, ints.joinToString(" ").toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND
	)
	Files.write(path, "\r\n".toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND)
}

fun nuke(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("nuke")) {
		if (!event.isGeneralMessage(data)) {
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().access(sc, Lang.NO_ACCESS.get(data))
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			if (arguments.size == 1) {
				sc.respondLater().thenAccept {
					try {
						val limit = arguments[0].toInt()
						if (limit >= 200 || limit <= 3) {
							throw Exception()
						}
						val arr = sc.channel.get().getMessages(limit).get().map { it.id }.toLongArray()
						sc.channel.get().bulkDelete(*arr)
						val embed = EmbedBuilder().success(sc, Lang.NUKE.get(data))
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					} catch (e: Exception) {
						val embed = EmbedBuilder().error(sc, Lang.INVALID_FORMAT.get(data))
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					}
				}
			} else {
				sc.respondLater().thenAccept {
					val embed = EmbedBuilder().error(sc, Lang.INVALID_ARG.get(data))
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}
			}
		}
	}
}

fun setChance(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("set_chance")) {
		if (!event.isOfficerMessage(data)) {
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().access(sc, Lang.NO_ACCESS.get(data))
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			if (arguments.size == 1) {
				sc.respondLater().thenAccept {
					try {
						val chance = arguments[0].toInt()
						data.chance = chance
						val embed = EmbedBuilder().success(sc, "${Lang.SET_CHANCE.get(data)}: $chance.")
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					} catch (e: Exception) {
						val embed = EmbedBuilder().error(sc, Lang.INVALID_FORMAT.get(data))
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					}
				}
			} else {
				sc.respondLater().thenAccept {
					val embed = EmbedBuilder().error(sc, Lang.INVALID_ARG.get(data))
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}
			}
		}
	}
}

fun clearServerMessages(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("clear_messages")) {
		if (!event.isGeneralMessage(data)) {
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().access(sc, Lang.NO_ACCESS.get(data))
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			sc.respondLater().thenAccept {
				val path = Paths.get("${data.serverID}/messages.bin")
				Files.write(path, byteArrayOf())
				val embed = EmbedBuilder().success(sc, Lang.CLEARED_MESSAGES.get(data))
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		}
	}
}