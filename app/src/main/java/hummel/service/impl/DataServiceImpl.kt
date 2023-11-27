package hummel.service.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hummel.bean.ServerData
import hummel.bean.ServerDataLegacy
import hummel.factory.DaoFactory
import hummel.service.DataService
import org.javacord.api.entity.server.Server
import java.time.LocalDate

class DataServiceImpl : DataService {
	private val dao = DaoFactory.dao

	override fun loadData(server: Server): ServerData {
		return readDataFromJson(server) ?: getDataFromDiscord(server)
	}

	override fun saveData(server: Server, data: ServerData) {
		saveDataToJson(server, data)
	}

	private fun getDataFromDiscord(server: Server): ServerData {
		val serverName = server.name
		val serverID = server.id.toString()
		val chance = 10

		val (folderPath, filePath) = serverID to "$serverID/messages.bin"

		dao.createFolder(folderPath)
		dao.createFile(filePath)

		val currentDate = LocalDate.now()
		val yesterday = currentDate.minusDays(1)
		val yesterdayDay = yesterday.dayOfMonth
		val yesterdayMonth = yesterday.monthValue
		val date = ServerData.Date(yesterdayDay, yesterdayMonth)
		val lang = "ru"

		return ServerData(serverID, serverName, chance, lang, date)
	}

	private fun readDataFromJson(server: Server): ServerData? {
		val serverID = server.id.toString()
		val filePath = "$serverID/data.json"
		val json = dao.readFromFile(filePath)
		val gson = Gson()

		try {
			return gson.fromJson(json, ServerData::class.java)
		} catch (ignored: Exception) {
			try {
				return gson.fromJson(json, ServerDataLegacy::class.java).convert()
			} catch (ignored: Exception) {
			}
		}
		return null
	}

	private fun saveDataToJson(server: Server, data: ServerData) {
		try {
			val serverID = server.id.toString()
			val gson = GsonBuilder().setPrettyPrinting().create()
			val json = gson.toJson(data)

			val filePath = "$serverID/data.json"

			dao.createFile(filePath)
			dao.writeToFile(filePath, json)

		} catch (ignored: Exception) {
		}
	}
}