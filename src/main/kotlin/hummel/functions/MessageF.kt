package hummel.functions

import hummel.rand
import hummel.structures.ServerData
import hummel.utils.getRandomLine
import org.javacord.api.event.interaction.InteractionCreateEvent
import org.javacord.api.event.message.MessageCreateEvent
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

fun setChance(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("set_chance")) {
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.size == 1) {
			try {
				data.chance = arguments[0].toInt()
				sc.createImmediateResponder().setContent("Chance changed to ${data.chance}.").respond()
			} catch (e: NumberFormatException) {
				sc.createImmediateResponder().setContent("Invalid argument format").respond()
			}
		} else {
			sc.createImmediateResponder().setContent("No arguments provided.").respond()
		}
	}
}

fun getServerMessages(event: InteractionCreateEvent, data: ServerData) {
	forkSendAndDelete(event, data, "get_messages", "messages", "bin")
}

fun getServerData(event: InteractionCreateEvent, data: ServerData) {
	forkSendAndDelete(event, data, "get_data", "data", "json")
}

fun clearServerMessages(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("clear_messages")) {
		val path = Paths.get("${data.serverID}/messages.bin")
		Files.write(path, byteArrayOf())
		sc.createImmediateResponder().setContent("Server messages cleared.").respond()
	}
}

fun forkSendAndDelete(
	event: InteractionCreateEvent, data: ServerData, command: String, fileName: String, fileExtension: String
) {

	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains(command)) {
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
				sc.createImmediateResponder().setContent("Error while copying and sending file.").respond()
			}
		}
	}
}