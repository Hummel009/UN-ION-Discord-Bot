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
		sc.respondLater().thenAccept {
			val embed = if (!event.isGeneralMessage(data)) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				if (arguments.size == 1) {
					try {
						val range = arguments[0].toInt()
						if (range !in 2..200) {
							throw Exception()
						}
						val messageIds = sc.channel.get().getMessages(range).get().map { it.id }.toLongArray()
						sc.channel.get().bulkDelete(*messageIds).get()
						EmbedBuilder().success(sc, data, Lang.NUKE.get(data))
					} catch (e: Exception) {
						EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
					}
				} else {
					EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				}
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}.get()
	}
}

fun setChance(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("set_chance")) {
		sc.respondLater().thenAccept {
			val embed = if (!event.isOfficerMessage(data)) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				if (arguments.size == 1) {
					try {
						val chance = arguments[0].toInt()
						if (chance == 0) {
							throw Exception()
						}
						data.chance = chance
						EmbedBuilder().success(sc, data, "${Lang.SET_CHANCE.get(data)}: $chance.")
					} catch (e: Exception) {
						EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
					}
				} else {
					EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				}
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}.get()
	}
}

fun clearServerMessages(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("clear_messages")) {
		sc.respondLater().thenAccept {
			val embed = if (!event.isGeneralMessage(data)) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				val path = Paths.get("${data.serverID}/messages.bin")
				Files.write(path, byteArrayOf())
				EmbedBuilder().success(sc, data, Lang.CLEARED_MESSAGES.get(data))
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}.get()
	}
}