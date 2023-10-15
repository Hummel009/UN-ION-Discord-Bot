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

	val filePath = Paths.get("db.bin")
	if (Files.notExists(filePath)) {
		Files.createFile(filePath)
	}

	var lastModifiedTime = Files.getLastModifiedTime(filePath).toMillis()
	var chance = 10

	api.addMessageCreateListener { event ->
		if (event.messageAuthor.isBotOwner) {
			if (event.messageContent == "!clear database") {
				Files.write(filePath, byteArrayOf())
				event.channel.sendMessage("Database cleared.")
			}
			if (event.messageContent.startsWith("!chance")) {
				val parts = event.messageContent.split(" ")
				if (parts.size >= 2) {
					try {
						chance = parts[1].toInt()
						event.channel.sendMessage("Chance changed to $chance.")
					} catch (e: NumberFormatException) {
						event.channel.sendMessage("Invalid integer format after !chance.")
					}
				} else {
					event.channel.sendMessage("No integer provided after !chance.")
				}
			}
		}
		if (!event.messageContent.isMessageForbidden() && !event.messageAuthor.isYourself && !event.messageAuthor.isBotUser) {
			val unicodeCodes = event.messageContent.codePoints().toArray()
			Files.write(
				filePath,
				unicodeCodes.joinToString(" ").toByteArray(StandardCharsets.UTF_8),
				StandardOpenOption.APPEND
			)
			Files.write(filePath, "\r\n".toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND)

			val currentModifiedTime = Files.getLastModifiedTime(filePath).toMillis()

			if (rand.nextInt(chance) == 0 && currentModifiedTime > lastModifiedTime) {
				val randomLine = filePath.getRandomLine()
				event.channel.sendMessage(randomLine)
				lastModifiedTime = currentModifiedTime
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