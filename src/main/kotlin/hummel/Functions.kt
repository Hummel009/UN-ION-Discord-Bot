package hummel

import org.javacord.api.event.message.MessageCreateEvent
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

fun sendMessage(event: MessageCreateEvent, data: ServerInfo) {
	val path = Paths.get("${data.serverID}/messages.bin")
	val editTime = Files.getLastModifiedTime(path).toMillis()

	if (rand.nextInt(data.chance) == 0 && editTime > data.lastUpdate) {
		val randomLine = path.getRandomLine()
		randomLine?.let {
			event.channel.sendMessage(it)
			data.lastUpdate = editTime
		}
	}
}

fun saveMessage(event: MessageCreateEvent, data: ServerInfo) {
	val path = Paths.get("${data.serverID}/messages.bin")
	val ints = event.messageContent.codePoints().toArray()
	Files.write(
		path, ints.joinToString(" ").toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND
	)
	Files.write(path, "\r\n".toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND)
}

fun registerClearFunc(event: MessageCreateEvent, data: ServerInfo) {
	if (event.messageContent == "!clear database") {
		val path = Paths.get("${data.serverID}/messages.bin")
		Files.write(path, byteArrayOf())
		event.channel.sendMessage("Database cleared.")
	}
}

fun registerChanceFunc(event: MessageCreateEvent, data: ServerInfo) {
	if (event.messageContent.startsWith("!chance")) {
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