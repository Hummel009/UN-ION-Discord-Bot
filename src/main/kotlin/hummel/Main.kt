package hummel

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.intent.Intent
import java.io.*
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

	api.addMessageCreateListener { event ->
		val serverID = event.server.get().id
		val serverName = event.server.get().name
		val chance = "10"

		val folderPath = Paths.get(serverID.toString())
		if (!Files.exists(folderPath)) {
			Files.createDirectories(folderPath)
		}

		val path = folderPath.resolve("messages.bin")
		if (!Files.exists(path)) {
			Files.createFile(path)
		}

		val lastUpdateDb = Files.getLastModifiedTime(path).toMillis()

		if (event.messageAuthor.isBotOwner) {
			if (event.messageContent == "!clear database") {
				Files.write(path, byteArrayOf())
				event.channel.sendMessage("Database cleared.")
			}
			if (event.messageContent.startsWith("!chance")) {
				val data = mutableMapOf(
					"serverID" to serverID.toString(),
					"serverName" to serverName,
					"chance" to chance,
					"lastUpdateDb" to lastUpdateDb.toString()
				)

				val parts = event.messageContent.split(" ")
				if (parts.size >= 2) {
					try {
						data["chance"] = parts[1].toInt().toString()
						saveDataToJson(data, "$serverID/data.json")
						event.channel.sendMessage("Chance changed to ${data["chance"]}.")
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

			var data = readDataFromJson("$serverID/data.json")

			if (data == null) {
				data = mutableMapOf(
					"serverID" to serverID.toString(),
					"serverName" to serverName,
					"chance" to chance,
					"lastUpdateDb" to lastUpdateDb.toString()
				)
				saveDataToJson(data, "$serverID/data.json")
			}

			if (rand.nextInt(data["chance"]?.toInt() ?: 10) == 0 && editTime > (data["lastUpdateDb"]?.toLong() ?: 0L)) {
				val randomLine = path.getRandomLine()
				randomLine?.let {
					event.channel.sendMessage(it)
					data["lastUpdateDb"] = editTime.toString()
					saveDataToJson(data, "$serverID/data.json")
				}
			}
		}
	}

	println("You can invite the bot by using the following url: " + api.createBotInvite())
}

fun String.isMessageForbidden(): Boolean {
	return this.length < 2 || startsWith("!") || startsWith("?") || contains("@") || contains("http") || contains("\r") || contains("\n")
}

fun Path.getRandomLine(): String? {
	val lines = Files.readAllLines(this)
	if (lines.isNotEmpty()) {
		val randomLine = lines[rand.nextInt(lines.size)]
		if (!randomLine.isMessageForbidden()) {
			val unicodeCodes = randomLine.split(" ").map { it.toInt() }
			val unicodeChars = unicodeCodes.map { it.toChar() }.toCharArray()
			return String(unicodeChars)
		}
		return null
	} else {
		return null
	}
}

fun saveDataToJson(data: Map<String, String>, filePath: String) {
	try {
		FileWriter(filePath).use {
			val gson = GsonBuilder().setPrettyPrinting().create()
			gson.toJson(data, it)
		}
	} catch (ignored: IOException) {
	}
}

fun readDataFromJson(filePath: String): MutableMap<String, String>? {
	try {
		BufferedReader(FileReader(filePath)).use { reader ->
			val gson = Gson()
			val mapType = object : TypeToken<Map<String, String>>() {}.type
			return gson.fromJson(reader, mapType)
		}
	} catch (ignored: IOException) {
	}
	return null
}