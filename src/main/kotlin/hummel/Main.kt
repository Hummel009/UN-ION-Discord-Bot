package hummel

import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.intent.Intent
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

val rand: Random = Random()

fun main() {
	val token = File("token.txt").readText(StandardCharsets.UTF_8)

	val api = DiscordApiBuilder().setToken(token).addIntents(*Intent.values()).login().join()

	val chance = HashMap<Long, Int>()
	val lastEditTime = HashMap<Long, Long>()

	api.addMessageCreateListener { event ->
		val serverID = event.server.get().id
		val folderPath = Paths.get("$serverID")
		if (!Files.exists(folderPath)) {
			Files.createDirectories(folderPath)
		}
		val path = folderPath.resolve("messages.bin")
		if (!Files.exists(path)) {
			Files.createFile(path)
		}

		lastEditTime[serverID] = Files.getLastModifiedTime(path).toMillis()

		if (event.messageAuthor.isBotOwner) {
			if (event.messageContent == "!clear database") {
				Files.write(path, byteArrayOf())
				event.channel.sendMessage("Database cleared.")
			}
			if (event.messageContent.startsWith("!chance")) {
				val parts = event.messageContent.split(" ")
				if (parts.size >= 2) {
					try {
						chance[serverID] = parts[1].toInt()
						event.channel.sendMessage("Chance changed to ${chance[serverID]}.")
					} catch (e: NumberFormatException) {
						event.channel.sendMessage("Invalid integer format after !chance.")
					}
				} else {
					event.channel.sendMessage("No integer provided after !chance.")
				}
			}
		}
		if (!event.messageContent.isMessageForbidden() && !event.messageAuthor.isYourself && !event.messageAuthor.isBotUser) {
			val ints = event.messageContent.codePoints().toArray()
			Files.write(
				path, ints.joinToString(" ").toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND
			)
			Files.write(path, "\r\n".toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND)

			val editTime = Files.getLastModifiedTime(path).toMillis()

			if (rand.nextInt(chance[serverID] ?: 10) == 0 && editTime > (lastEditTime[serverID] ?: 0L)) {
				val randomLine = path.getRandomLine()
				event.channel.sendMessage(randomLine)
				lastEditTime[event.server.get().id] = editTime
			}
		}
	}

	println("You can invite the bot by using the following url: " + api.createBotInvite())
}

fun String.isMessageForbidden(): Boolean {
	return this.length < 2 || startsWith("!") || contains("@") || contains("http") || contains("\r") || contains("\n")
}

fun Path.getRandomLine(): String {
	val lines = Files.readAllLines(this)
	return if (lines.isNotEmpty()) {
		val randomLine = lines[rand.nextInt(lines.size)]
		val unicodeCodes = randomLine.split(" ").map { it.toInt() }
		val unicodeChars = unicodeCodes.map { it.toChar() }.toCharArray()
		String(unicodeChars)
	} else {
		""
	}
}