package hummel.dao.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hummel.bean.ServerData
import hummel.bean.ServerDataLegacy
import hummel.dao.FileDao
import hummel.dao.JsonDao
import hummel.factory.DaoFactory
import hummel.utils.version
import org.javacord.api.entity.server.Server

class JsonDaoImpl : JsonDao {
	private val fileDao: FileDao = DaoFactory.fileDao

	override fun readFromJson(server: Server): ServerData? {
		val serverId = server.id.toString()
		val filePath = "$serverId/data.json"
		var json: ByteArray
		val gson = Gson()

		try {
			json = fileDao.readFromFile(filePath)
			val data = gson.fromJson(String(json), ServerData::class.java)
			if (data.dataVer != version) {
				throw Exception()
			}
			return data
		} catch (e: Exception) {
			println("LEGACY DATA DETECTED!")
			try {
				json = fileDao.readFromFile(filePath)
				return gson.fromJson(String(json), ServerDataLegacy::class.java).convert()
			} catch (ignored: Exception) {
			}
		}
		return null
	}

	override fun writeToJson(server: Server, data: ServerData) {
		try {
			val serverId = server.id.toString()
			val gson = GsonBuilder().setPrettyPrinting().create()
			val json = gson.toJson(data)

			val filePath = "$serverId/data.json"

			fileDao.createFile(filePath)
			fileDao.writeToFile(filePath, json.toByteArray())

		} catch (ignored: Exception) {
		}
	}
}