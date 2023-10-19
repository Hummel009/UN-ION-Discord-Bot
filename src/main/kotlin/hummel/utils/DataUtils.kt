package hummel.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hummel.structures.Date
import hummel.structures.ServerData
import org.javacord.api.event.message.MessageCreateEvent
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

fun getDataFromDiscord(event: MessageCreateEvent, serverID: String): ServerData {
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

	val currentDate = LocalDate.now()
	val yesterday = currentDate.minusDays(1)
	val yesterdayDay = yesterday.dayOfMonth
	val yesterdayMonth = yesterday.monthValue

	return ServerData(serverID, serverName, chance, HashSet(), Date(yesterdayDay, yesterdayMonth))
}

fun saveDataToJson(data: ServerData, filePath: String) {
	try {
		val gson = GsonBuilder().setPrettyPrinting().create()
		val json = gson.toJson(data)
		Files.write(Paths.get(filePath), json.toByteArray())
	} catch (ignored: IOException) {
	}
}

fun readDataFromJson(filePath: String): ServerData? {
	try {
		val gson = Gson()
		val json = String(Files.readAllBytes(Paths.get(filePath)))
		return gson.fromJson(json, ServerData::class.java)
	} catch (ignored: IOException) {
	}
	return null
}