package hummel.utils

import android.content.Context
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hummel.structures.ServerData
import org.javacord.api.entity.server.Server
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

fun getDataFromDiscord(context: Context, server: Server, serverID: String): ServerData {
	val serverName = server.name
	val chance = 10

	val downloadsDir = context.filesDir
	val folderPath = File(downloadsDir, serverID)

	if (!folderPath.exists()) {
		folderPath.mkdirs()
	}

	val msgPath = File(folderPath, "messages.bin")

	if (!msgPath.exists()) {
		try {
			msgPath.createNewFile()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	val currentDate = LocalDate.now()
	val yesterday = currentDate.minusDays(1)
	val yesterdayDay = yesterday.dayOfMonth
	val yesterdayMonth = yesterday.monthValue
	val date = ServerData.Date(yesterdayDay, yesterdayMonth)
	val lang = "ru"

	return ServerData(serverID, serverName, chance, lang, date, HashSet(), HashSet(), HashSet())
}

fun saveDataToJson(context: Context, data: ServerData, filePath: String) {
	try {
		val gson = GsonBuilder().setPrettyPrinting().create()
		val json = gson.toJson(data)

		if (isExternalStorageWritable()) {
			val downloadsDir = context.filesDir
			val file = File(downloadsDir, filePath)

			val writer = FileWriter(file)
			writer.use {
				it.write(json)
			}
		}
	} catch (e: IOException) {
		e.printStackTrace()
	}
}

fun readDataFromJson(context: Context, filePath: String): ServerData? {
	try {
		val downloadsDir = context.filesDir
		val file = File(downloadsDir, filePath)

		if (file.exists()) {
			val gson = Gson()
			val json = String(Files.readAllBytes(Paths.get(file.path)))
			return gson.fromJson(json, ServerData::class.java)
		}
	} catch (e: IOException) {
		e.printStackTrace()
	}

	return null
}

private fun isExternalStorageWritable(): Boolean {
	return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
}