package hummel.functions

import hummel.structures.ServerData
import hummel.utils.isOfficerMessage
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

	if (!event.isOfficerMessage(data)) {
		sc.createImmediateResponder().setContent("You do not have permission to use this command.").respond()
		return
	}

	if (sc.fullCommandName.contains("get_$fileName")) {
		sc.respondLater().thenAccept {
			val path = Paths.get("${data.serverID}/$fileName.$fileExtension")

			val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
			val destinationPath = Paths.get("${data.serverID}/$fileName-$timeStamp.$fileExtension")
			try {
				Files.copy(path, destinationPath)
				val backupFile = File(destinationPath.toString())
				val future = sc.createFollowupMessageBuilder().addAttachment(backupFile).send()
				future.get()
				Files.delete(destinationPath)
			} catch (e: Exception) {
				sc.createImmediateResponder().setContent("Error when copying and sending a file.").respond()
			}
		}
	}
}