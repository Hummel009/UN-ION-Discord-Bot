package hummel.functions

import hummel.structures.ServerData
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.error
import hummel.utils.isOfficerMessage
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			sc.respondLater().thenAccept {
				try {
					val path = Paths.get("${data.serverID}/$fileName.$fileExtension")
					val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
					val destinationPath = Paths.get("${data.serverID}/$fileName-$timeStamp.$fileExtension")
					Files.copy(path, destinationPath)
					val backupFile = File(destinationPath.toString())
					sc.createFollowupMessageBuilder().addAttachment(backupFile).send().get()
					Files.delete(destinationPath)
				} catch (e: Exception) {
					val embed = EmbedBuilder().error(sc, data, Lang.BACKUP_ERROR.get(data))
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}
			}
		}
	}
}