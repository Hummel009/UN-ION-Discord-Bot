package com.github.hummel.union.service

import com.github.hummel.union.bean.ServerData
import org.javacord.api.entity.server.Server
import org.javacord.api.interaction.SlashCommandInteraction

interface DataService {
	fun loadServerData(server: Server): ServerData
	fun saveServerData(server: Server, serverData: ServerData)
	fun exportBotData(sc: SlashCommandInteraction)
	fun importBotData(byteArray: ByteArray)
	fun saveServerMessage(server: Server, msg: String)
	fun getServerRandomMessage(server: Server): String?
	fun wipeServerMessages(server: Server)
	fun wipeServerData(server: Server)
}