package hummel.service

import hummel.bean.ServerData
import org.javacord.api.entity.server.Server

interface DataService {
	fun loadServerData(server: Server): ServerData
	fun saveServerData(server: Server, serverData: ServerData)
}