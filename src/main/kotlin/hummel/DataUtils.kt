package hummel

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

fun getData(event: MessageCreateEvent, serverID: String): ServerInfo {
	val serverName = event.server.get().name
	val chance = 10

	val folderPath = Paths.get(serverID)
	if (!Files.exists(folderPath)) {
		Files.createDirectories(folderPath)
	}

	val msgPath = Paths.get("$serverID/messages.bin")
	if (!Files.exists(msgPath)) {
		Files.createFile(msgPath)
	}

	val lastUpdate = Files.getLastModifiedTime(msgPath).toMillis()

	return ServerInfo(serverID, serverName, chance, lastUpdate)
}

fun saveDataToJson(data: ServerInfo, filePath: String) {
	try {
		val gson = GsonBuilder().setPrettyPrinting().create()
		val json = gson.toJson(data)
		Files.write(Paths.get(filePath), json.toByteArray())
	} catch (ignored: IOException) {
	}
}

fun readDataFromJson(filePath: String): ServerInfo? {
	try {
		val gson = Gson()
		val json = String(Files.readAllBytes(Paths.get(filePath)))
		return gson.fromJson(json, ServerInfo::class.java)
	} catch (ignored: IOException) {
	}
	return null
}