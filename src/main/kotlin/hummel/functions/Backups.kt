package hummel.functions

import hummel.structures.ServerData
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.isOfficerMessage
import hummel.utils.success
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun getCommands(event: InteractionCreateEvent, data: ServerData, api: DiscordApi) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("get_commands")) {
		sc.respondLater().thenAccept {
			val embed = if (!event.isOfficerMessage(data)) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				val text = buildString {
					api.globalApplicationCommands.get().joinTo(this, "\r\n") { "${it.name}: ${it.id}" }
				}
				EmbedBuilder().success(sc, data, text)
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}.get()
	}
}

fun getServerMessages(event: InteractionCreateEvent, data: ServerData) {
	forkSendAndDelete(event, data, "messages", "bin")
}

fun getServerData(event: InteractionCreateEvent, data: ServerData) {
	forkSendAndDelete(event, data, "data", "json")
}

fun forkSendAndDelete(
	event: InteractionCreateEvent, data: ServerData, fileName: String, fileExtension: String
) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("get_$fileName")) {
		if (!event.isOfficerMessage(data)) {
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				sc.createFollowupMessageBuilder().addEmbed(embed).send()
			}.get()
		} else {
			val destinationPath = Paths.get("${data.serverID}/$fileName-backup.$fileExtension")
			sc.respondLater().thenAccept {
				val path = Paths.get("${data.serverID}/$fileName.$fileExtension")
				Files.copy(path, destinationPath)
				val backupFile = File(destinationPath.toString())
				sc.createFollowupMessageBuilder().addAttachment(backupFile).send()
			}.get()
			Files.delete(destinationPath)
		}
	}
}