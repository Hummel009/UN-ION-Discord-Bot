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
	println("Connecting to Discord...")
	val token = File("token.txt").readText(StandardCharsets.UTF_8)

	val api = DiscordApiBuilder().setToken(token).addIntents(*Intent.values()).login().join()

	val filePath = Paths.get("db.txt")
	if (Files.notExists(filePath)) {
		Files.createFile(filePath)
	}

	var lastModifiedTime = Files.getLastModifiedTime(filePath).toMillis()
	var chance = 10

	api.addMessageCreateListener { event ->
		if (event.messageAuthor.isBotOwner) {
			if (event.messageContent.equals("!clear database")) {
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
			val messageToSave = "${event.messageContent}\r\n"
			Files.write(filePath, messageToSave.toByteArray(), StandardOpenOption.APPEND)

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
	return if (lines.isNotEmpty()) lines[Random().nextInt(lines.size)] else ""
}