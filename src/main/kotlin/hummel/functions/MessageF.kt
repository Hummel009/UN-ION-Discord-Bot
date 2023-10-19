package hummel.functions

import hummel.functions
import hummel.prefix
import hummel.rand
import hummel.structures.ServerData
import hummel.utils.getRandomLine
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

fun setMessageChance(event: MessageCreateEvent, data: ServerData) {
	if (event.messageContent.startsWith("${prefix}set_chance")) {
		functions.add("set_chance INT")
		val parameters = event.messageContent.split(" ")
		if (parameters.size == 2) {
			try {
				data.chance = parameters[1].toInt()
				event.channel.sendMessage("Chance changed to ${data.chance}.")
			} catch (e: NumberFormatException) {
				event.channel.sendMessage("Invalid integer format after !chance.")
			}
		} else {
			event.channel.sendMessage("No integer provided after !chance.")
		}
	}
}

fun getServerMessages(event: MessageCreateEvent, data: ServerData) {
	forkSendAndDelete(event, data, "get_messages", "messages", "bin")
}

fun getServerData(event: MessageCreateEvent, data: ServerData) {
	forkSendAndDelete(event, data, "get_data", "data", "json")
}

fun getHelp(event: MessageCreateEvent) {
	val sb = StringBuilder()
	sb.append("All the commands of the bot:\r\n")

	functions.forEach {
		sb.append(">> $prefix$it;\r\n")
	}

	event.channel.sendMessage(sb.toString())
}

fun clearServerMessages(event: MessageCreateEvent, data: ServerData) {
	if (event.messageContent == "${prefix}clear_messages") {
		functions.add("clear_messages")
		val path = Paths.get("${data.serverID}/messages.bin")
		Files.write(path, byteArrayOf())
		event.channel.sendMessage("Server messages cleared.")
	}
}

fun forkSendAndDelete(
	event: MessageCreateEvent, data: ServerData, command: String, fileName: String, fileExtension: String
) {
	if (event.messageContent == "$prefix$command") {
		functions.add(command)
		val path = Paths.get("${data.serverID}/$fileName")

		val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
		val destinationPath = Paths.get("${data.serverID}/$fileName-$timeStamp.$fileExtension")

		try {
			Files.copy(path, destinationPath)
			val backupFile = File(destinationPath.toString())
			val future = event.channel.sendMessage(backupFile)
			future.get()
			Files.delete(destinationPath)
		} catch (e: Exception) {
			event.channel.sendMessage("Error while copying and sending file.")
		}
	}
}