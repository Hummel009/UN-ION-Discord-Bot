package hummel.service.impl

import hummel.bean.ServerData
import hummel.dao.FileDao
import hummel.dao.JsonDao
import hummel.factory.DaoFactory
import hummel.service.DataService
import hummel.utils.version
import org.javacord.api.entity.server.Server
import java.time.LocalDate

class DataServiceImpl : DataService {
	private val fileDao: FileDao = DaoFactory.fileDao
	private val jsonDao: JsonDao = DaoFactory.jsonDao

	override fun loadServerData(server: Server): ServerData {
		val folderName = server.id.toString()
		val filePath = "$folderName/data.json"
		return jsonDao.readFromJson(filePath, ServerData::class.java) ?: getServerDataFromDiscord(server)
	}

	override fun saveServerData(server: Server, serverData: ServerData) {
		val folderName = server.id.toString()
		val filePath = "$folderName/data.json"
		jsonDao.writeToJson(filePath, serverData)
	}

	private fun getServerDataFromDiscord(server: Server): ServerData {
		val serverName = server.name
		val serverId = server.id.toString()
		val chance = 10

		val (folderPath, filePath) = serverId to "$serverId/messages.bin"

		fileDao.createFolder(folderPath)
		fileDao.createFile(filePath)

		val currentDate = LocalDate.now()
		val yesterday = currentDate.minusDays(1)
		val yesterdayDay = yesterday.dayOfMonth
		val yesterdayMonth = yesterday.monthValue
		val date = ServerData.Date(yesterdayDay, yesterdayMonth)
		val lang = "ru"

		return ServerData(
			dataVer = version,
			serverId = serverId,
			serverName = serverName,
			chance = chance,
			lang = lang,
			lastWish = date,
			secretChannels = mutableSetOf(),
			managers = mutableSetOf(),
			birthdays = mutableSetOf(),
		)
	}
}