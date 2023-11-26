package hummel.functions

import android.content.Context
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
			sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
		}.get()
	}
}

fun getServerMessages(context: Context, event: InteractionCreateEvent, data: ServerData) {
	forkSendAndDelete(context, event, data, "messages", "bin")
}

fun getServerData(context: Context, event: InteractionCreateEvent, data: ServerData) {
	forkSendAndDelete(context, event, data, "data", "json")
}

fun forkSendAndDelete(
	context: Context, event: InteractionCreateEvent, data: ServerData, fileName: String, fileExtension: String
) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("get_$fileName")) {
		if (!event.isOfficerMessage(data)) {
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		} else {
			val downloadsDir = context.filesDir
			val destinationPath = File(downloadsDir, "${data.serverID}/$fileName-backup.$fileExtension")

			sc.respondLater().thenAccept {
				val path = File(downloadsDir, "${data.serverID}/$fileName.$fileExtension")
				Files.copy(path.toPath(), destinationPath.toPath())
				sc.createFollowupMessageBuilder().addAttachment(destinationPath).send().get()
			}.get()

			Files.deleteIfExists(destinationPath.toPath())
		}
	}
}