package hummel.dao

import hummel.bean.ServerData
import org.javacord.api.entity.server.Server

interface JsonDao {
	fun readFromJson(server: Server): ServerData?
	fun writeToJson(server: Server, data: ServerData)
}