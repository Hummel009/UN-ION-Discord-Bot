package hummel.service.impl

import hummel.bean.ServerData
import hummel.dao.FileDao
import hummel.dao.JsonDao
import hummel.dao.ZipDao
import hummel.factory.DaoFactory
import hummel.service.DataService
import hummel.utils.version
import org.javacord.api.entity.server.Server
import org.javacord.api.interaction.SlashCommandInteraction
import java.time.LocalDate

class DataServiceImpl : DataService {
	private val fileDao: FileDao = DaoFactory.fileDao
	private val jsonDao: JsonDao = DaoFactory.jsonDao
	private val zipDao: ZipDao = DaoFactory.zipDao

	override fun loadServerData(server: Server): ServerData {
		val folderName = server.id.toString()
		val filePath = "servers/$folderName/data.json"
		return jsonDao.readFromJson(filePath, ServerData::class.java) ?: getServerDataFromDiscord(server)
	}

	override fun saveServerData(server: Server, serverData: ServerData) {
		val folderName = server.id.toString()
		val filePath = "servers/$folderName/data.json"
		jsonDao.writeToJson(filePath, serverData)
	}

	override fun exportBotData(sc: SlashCommandInteraction) {
		val folderPath = "servers"
		val filePath = "archive/bot.zip"

		zipDao.zipFolder(folderPath, filePath)
		val file = fileDao.getFile(filePath)
		sc.createFollowupMessageBuilder().addAttachment(file).send().get()
		fileDao.removeFile(filePath)
	}

	override fun importBotData(byteArray: ByteArray) {
		val folderPath = "servers"
		val filePath = "archive/bot.zip"

		fileDao.createFile(filePath)
		fileDao.writeToFile(filePath, byteArray)
		zipDao.unzipFile(filePath, folderPath)
		fileDao.removeFile(filePath)
	}

	override fun saveServerMessage(server: Server, msg: String) {
		val folderName = server.id.toString()
		val filePath = "servers/$folderName/messages.bin"
		fileDao.appendToFile(filePath, msg.toByteArray())
		fileDao.appendToFile(filePath, "\r\n".toByteArray())
	}

	override fun getServerRandomMessage(server: Server): String? {
		val folderName = server.id.toString()
		val filePath = "servers/$folderName/messages.bin"
		return fileDao.getRandomLine(filePath)
	}

	override fun wipeServerMessages(server: Server) {
		val folderName = server.id.toString()
		val filePath = "servers/$folderName/messages.bin"
		fileDao.removeFile(filePath)
		fileDao.createFile(filePath)
	}

	override fun wipeServerData(server: Server) {
		val folderName = server.id.toString()
		val filePath = "servers/$folderName/data.json"
		fileDao.removeFile(filePath)
		fileDao.createFile(filePath)
	}

	private fun getServerDataFromDiscord(server: Server): ServerData {
		val folderName = server.id.toString()
		val folderPath = "servers/$folderName"
		val filePath = "servers/$folderName/messages.bin"

		fileDao.createFolder(folderPath)
		fileDao.createFile(filePath)

		val serverId = server.id.toString()
		val serverName = server.name
		val chance = 10
		val lang = "ru"
		val yesterday = LocalDate.now().minusDays(1)
		val lastWish = ServerData.Date(yesterday.dayOfMonth, yesterday.monthValue)

		return ServerData(
			dataVer = version,
			serverId = serverId,
			serverName = serverName,
			chance = chance,
			lang = lang,
			lastWish = lastWish,
			secretChannels = mutableSetOf(),
			managers = mutableSetOf(),
			birthdays = mutableSetOf(),
		)
	}
}