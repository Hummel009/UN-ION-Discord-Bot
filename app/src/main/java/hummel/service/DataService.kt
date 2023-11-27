package hummel.service

import hummel.bean.ServerData
import org.javacord.api.entity.server.Server

interface DataService {
	fun loadData(server: Server): ServerData
	fun saveData(server: Server, data: ServerData)
}