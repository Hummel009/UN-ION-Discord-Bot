package hummel.service.impl

import hummel.bean.ServerData
import hummel.dao.FileDao
import hummel.dao.JsonDao
import hummel.dataVer
import hummel.factory.DaoFactory
import hummel.service.DataService
import org.javacord.api.entity.server.Server
import java.time.LocalDate

class DataServiceImpl : DataService {
	private val fileDao: FileDao = DaoFactory.fileDao
	private val jsonDao: JsonDao = DaoFactory.jsonDao

	override fun loadData(server: Server): ServerData = jsonDao.readFromJson(server) ?: getDataFromDiscord(server)

	override fun saveData(server: Server, data: ServerData) {
		jsonDao.writeToJson(server, data)
	}

	private fun getDataFromDiscord(server: Server): ServerData {
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
			dataVer = dataVer,
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